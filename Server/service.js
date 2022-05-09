// < Require >
let request = require('request');

// << Path Class >>
module.exports.Service = class Service {
    // < Construct Path >
    constructor(connection, socket, data) {
        this.connection = connection;
        this.socket = socket;
        this.data = data;
        this.path = {
            "origin": { "name": "Vehicle", "x": 0, "y": 0 },
            "destination": { "name": null, "x": 0, "y": 0 },
            "waypoints": [],
            "priority": "RECOMMEND", "car_fuel": "GASOLINE", "car_hipass": false, "alternatives": false, "road_details": false, "summary": true
        }
        this.pathReq = {
            headers: { 'content-type': 'application/json', 'authorization': 'KakaoAK d94b5c67305d6a10b3e43e5da881e7cf' },
            url: 'https://apis-navi.kakaomobility.com/v1/waypoints/directions',
            body: null,
            json: true
        }
    }

    // < Set vehicle >
    async setVehicle() {
        // Search vehicles available
        let sql = this.data.share ? `select * from Vehicle where state = true and num != 3 and num != 0 and share = 1 and gender = ${this.data.user.gender}` : `select * from Vehicle where state = true and num = 0`;
        let [vehicles, field] = await this.connection.query(sql);
        let nearestIndex = -1;
        let minDistance = Number.MAX_VALUE;

        this.pointss = [];
        // Select nearest vehicle which is available
        for (let i = 0; i < vehicles.length; i++) {
            if (this.data.share) {
                let [flag, points] = this.checkInnerPath(vehicles[i]);
                pointss.push(points);
                if (!flag)
                    continue;
            }
            let position = vehicles[i].pos.split(' ');
            let distance = Math.sqrt(Math.pow(this.data.start.x - position[0], 2) + Math.pow(this.data.start.y - position[1], 2));
            nearestIndex = minDistance > distance ? i : nearestIndex;
            minDistance = minDistance > distance ? distance : minDistance;
        }

        // Search if share request has no vehicle
        if (this.data.share && nearestIndex == -1) {
            [vehicles, field] = await this.connection.query(`select * from Vehicle where state = true and num = 0`);
            for (let i = 0; i < vehicles.length; i++) {
                let position = vehicles[i].pos.split(' ');
                let distance = Math.sqrt(Math.pow(this.data.start.x - position[0], 2) + Math.pow(this.data.start.y - position[1], 2));
                nearestIndex = minDistance > distance ? i : nearestIndex;
                minDistance = minDistance > distance ? distance : minDistance;
            }
        }

        // Set vehicle
        if (nearestIndex != -1) {
            this.vehicle = vehicles[nearestIndex];
            this.pointss = this.pointss[nearestIndex];
        } else {
            this.vehicle = null;
        }
        this.vehicle = nearestIndex != -1 ? vehicles[nearestIndex] : null;
    }

    // < Set path >
    setPath() {
        if (this.data.share) {
            let points = this.pointss;
            this.path.origin = points[0];
            this.path.destination = points[points.length - 1];
            this.path.waypoints = points.slice(1, points.length - 1);
        } else {
            this.path.origin.x = this.vehicle.pos.split(' ')[0];
            this.path.origin.y = this.vehicle.pos.split(' ')[1];
            this.path.destination = this.data.end;
            this.path.waypoints.unshift(this.data.start);
        }
        this.pathReq.body = this.path;
    }

    // < Request path info >
    reqPath() {
        return new Promise((resolve, reject) => {
            request.post(this.pathReq, (err, httpResponse, body) => {
                if (!err && httpResponse.statusCode == 200) {
                    resolve(body.routes[0]);
                } else {
                    reject(err);
                }
            });
        });
    }

    // < Check if user coordinates are available >
    checkInnerPath(vehicle) {
        let start = this.data.start;
        let end = this.data.end;
        let path = JSON.parse(vehicle.unpass);
        let points = path.waypoints;
        points.unshift(path.origin);
        points.push(path.destination);

        let startIndex, endIndex, innerStart, innerEnd;

        // Search starting point range
        for (let s = 0; s < points.length - 1; s++) {
            let prePoint = points[s];
            let postPoint = points[s + 1];
            innerStart = this.calculateInnerPoint(prePoint, start, postPoint);
            if (innerStart) {
                startIndex = s + 1;
                break;
            }
        }

        // Search ending point range
        for (let e = startIndex - 1; e < points.length - 1; e++) {
            let prePoint = points[e];
            let postPoint = points[e + 1];
            innerEnd = this.calculateInnerPoint(prePoint, end, postPoint);
            if (innerEnd) {
                endIndex = e + 1;
                break;
            }
        }

        // Check availability of new point
        if (innerStart && innerEnd) {
            // Get pre/post point
            let prePoint = points[startIndex - 1];
            let postPoint = points[endIndex];
            // Calculate distance (Prepoint)
            let preTOstart = Math.pow(prePoint.x - start.x, 2) + Math.pow(prePoint.y - start.y, 2);
            let preTOend = Math.pow(prePoint.x - end.x, 2) + Math.pow(prePoint.y - end.y, 2);
            // Calculate distance (Postpoint)
            let startTOpost = Math.pow(postPoint.x - start.x, 2) + Math.pow(postPoint.y - start.y, 2);
            let endTOpost = Math.pow(postPoint.x - end.x, 2) + Math.pow(postPoint.y - end.y, 2);
            // Return (When start point is far than end point) ? true : false
            if (startIndex == endIndex && !(preTOstart < preTOend && startTOpost > endTOpost))
                return [false, null];
            else {
                points.splice(endIndex, 0, end);
                points.splice(startIndex, 0, start);
                return [true, points];
            }
        } else if (innerStart) {
            // Get last point end pre last point
            let lastPoint = points[points.length - 1];
            let preLastPoint = points[points.length - 2];
            // Get vector of prelast->last and last->end
            let uva = [lastPoint.x - preLastPoint.x, lastPoint.y - preLastPoint.y];
            let uvb = [end.x - lastPoint.x, end.y - lastPoint.y];
            // Get angle between prelast->last asd last->end
            let theta = Math.acos((uva[0] * uvb[0] + uva[1] * uvb[1]) / (Math.sqrt(Math.pow(uva[0], 2) + Math.pow(uva[1], 2)) * Math.sqrt(Math.pow(uvb[0], 2) + Math.pow(uvb[1], 2))));
            // Available when angle is smaller than 45 degrees
            if (theta < Math.PI / 4) {
                points.push(end);
                points.splice(startIndex, 0, start);
                return [true, points];
            } else
                return [false, null];
        } else
            return [false, null];
    }

    // < Calculate if coordinate is in single path >
    calculateInnerPoint(prePoint, point, postPoint) {
        // Set vectors
        let preTOpostV = [postPoint.x - prePoint.x, postPoint.y - prePoint.y];
        let postTOpreV = [prePoint.x - postPoint.x, prePoint.y - postPoint.y];
        let preTOpointV = [point.x - prePoint.x, point.y - prePoint.y];
        let postTOpointV = [point.x - postPoint.x, point.y - postPoint.y];

        // Available when angle is smaller than 30 degrees
        let preTheta = Math.acos((preTOpostV[0] * preTOpointV[0] + preTOpostV[1] * preTOpointV[1]) / (Math.sqrt(Math.pow(preTOpostV[0], 2) + Math.pow(preTOpostV[1], 2)) * Math.sqrt(Math.pow(preTOpointV[0], 2) + Math.pow(preTOpointV[1], 2))));
        let postTheta = Math.acos((postTOpreV[0] * postTOpointV[0] + postTOpreV[1] * postTOpointV[1]) / (Math.sqrt(Math.pow(postTOpreV[0], 2) + Math.pow(postTOpreV[1], 2)) * Math.sqrt(Math.pow(postTOpointV[0], 2) + Math.pow(postTOpointV[1], 2))));
        return preTheta < Math.PI / 6 && postTheta < Math.PI / 6 ? true : false;
    }

    // < Update Database >
    async updateDB() {
        let summary = this.path.summary;
        this.path = {};
        this.path.origin = summary.origin;
        this.path.destination = summary.destination;
        this.path.waypoints = summary.waypoints;
        this.path.distance = summary.distance;
        this.path.duration = summary.duration;
        this.path.sections = this.path.sections;

        let names = {};
        names = this.data.share ? JSON.parse(this.vehicle.names) : {};
        names[`${this.data.start.x}_${this.data.start.y}_${this.data.start.name}`] = {'user': this.data.user.usn, 'type': 'start'};
        names[`${this.data.end.x}_${this.data.end.y}_${this.data.end.name}`] = {'user': this.data.user.usn, 'type': 'end'};

        await this.connection.query(`update Vehicle set num = ${this.vehicle.num + 1}, unpass = '${JSON.stringify(this.path)}', share = ${this.data.share}, gender = ${this.data.user.gender}, cost = ${summary.fare.taxi}, names = '${JSON.stringify(names)}' where vsn = '${this.vehicle.vsn}'`);
        await this.connection.query(`update User set state = true where usn = ${this.data.user.usn}`);
    }
}