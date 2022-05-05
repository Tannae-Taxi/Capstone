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
            headers: {
                'content-type': 'application/json',
                'authorization': 'KakaoAK d94b5c67305d6a10b3e43e5da881e7cf'
            },
            url: 'https://apis-navi.kakaomobility.com/v1/waypoints/directions',
            body: null,
            json: true
        }
    }

    async setVehicle() {
        if (this.data.share) {
            let [vehicles, field] = await this.connection.query(`select * from Vehicle where state = true and num != 3 and num != 0 and share = 1 and gender = ${this.data.user.gender}`);
            let nearestIndex = -1;
            let minDistance = Number.MAX_VALUE;
            for (let i = 0; i < vehicles.length; i++) {
                if (!this.checkInnerPath(vehicles[i]))
                    delete vehicles[i];
                    // 추려진 vechiles 중에 어떤 vehilce을 선택할지 결정
            }
        } else {
            let [vehicles, field] = await this.connection.query(`select * from Vehicle where state = true and num = 0`);
            let nearestIndex = -1;
            let minDistance = Number.MAX_VALUE;
            for (let i = 0; i < vehicles.length; i++) {
                let position = vehicles[i].pos.split(' ');
                let distance = Math.sqrt(Math.pow(this.data.start.x - position[0], 2) + Math.pow(this.data.start.y - position[1], 2));
                nearestIndex = minDistance > distance ? i : nearestIndex;
                minDistance = minDistance > distance ? distance : minDistance;
            }
            this.vehicle = vehicles[nearestIndex];
        }

        // vehicle length가 0일 때를 처리 => 서비스 요청 거부
    }

    async setPath() {
        if (this.data.share) {

        } else {
            this.path.origin.x = this.vehicle.pos.split(' ')[0];
            this.path.origin.y = this.vehicle.pos.split(' ')[1];
            this.path.destination = this.data.end;
            this.path.waypoints.push(this.data.start);
        }
        this.pathReq.body = this.path;
    }

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

    async updateDB() {
        let summary = this.path.summary;
        this.path = {};
        this.path.origin = summary.origin;
        this.path.destination = summary.destination;
        this.path.waypoints = summary.waypoints;
        this.path.distance = summary.distance;
        this.path.duration = summary.duration;
        this.path.sections = this.path.sections;
        await this.connection.query(`update Vehicle set num = ${this.vehicle.num + 1}, unpass = '${JSON.stringify(this.path)}', share = ${this.data.share}, gender = ${this.data.user.gender}, cost = ${summary.fare.taxi} where vsn = '${this.vehicle.vsn}'`);
    }

    checkInnerPath(vehicle) {
        let start = this.data.start;
        let end = this.data.end;
        let path = JSON.parse(vehicle.unpass);
        let points = path.waypoints;
        points.unshift(path.origin);
        points.push(destination);

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
            let prePoint = points[startIndex - 1];
            let postPoint = points[endIndex];
            let preTOstart = Math.pow(prePoint.x - start.x, 2) + Math.pow(prePoint.y - start.y, 2);
            let preTOend = Math.pow(prePoint.x - end.x, 2) + Math.pow(prePoint.y - end.y, 2);
            let startTOpost = Math.pow(postPoint.x - start.x, 2) + Math.pow(postPoint.y - start.y, 2);
            let endTOpost = Math.pow(postPoint.x - end.x, 2) + Math.pow(postPoint.y - end.y, 2);
            return startIndex == endIndex ? preTOstart < preTOend && startTOpost > endTOpost : true;
        } else if (innerStart) {
            // start point는 inner point이지만 end point는 outer point (end point가 마지막 좌표 기준으로 마지막 직전 좌표와 반대 방향에 있으면 true 아니면 false)
        } else
            return false;
    }

    calculateInnerPoint(prePoint, point, postPoint) {
        // point가 prePoint와 postPoint의 범위 안에 있는지 체크
    }
}