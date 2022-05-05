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
}