// < Require >
let request = require('request');

// << Path Class >>
module.exports.Path = class Path {
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
            let [vehicles, field] = await this.connection.query(`select * from Vehicle where num != 3 and num != 0 and gender = ${this.data.user.gender}`);
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
    }

    async setPath() {
        if (this.data.share) {

        } else {
            this.path.origin.x = this.vehicle.pos.split(' ')[0];
            this.path.origin.y = this.vehicle.pos.split(' ')[1];
            this.path.destination.name = this.data.end.name;
            this.path.destination.x = this.data.end.x;
            this.path.destination.y = this.data.end.y;
            let jsonWay = {"name": this.data.start.name, "x": this.data.start.x, "y": this.data.start.y};
            this.path.waypoints.push(jsonWay);
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
        
    }
}

function getSinglePathDB(path, data, vsn) {
    let summary = path.summary;
    let sections = path.sections;
    return [
        {
            "name": summary.origin.name,
            "x": summary.origin.x,
            "y": summary.origin.y,
            "distance": 0,
            "duration": 0,
            "type": "taxi",
            "usn": vsn,
        },
        {
            "name": summary.waypoints[0].name,
            "x": summary.waypoints[0].x,
            "y": summary.waypoints[0].y,
            "distance": sections[0].distance,
            "duration": sections[0].duration,
            "type": "start",
            "usn": data.usn
        },
        {
            "name": summary.destination.name,
            "x": summary.destination.x,
            "y": summary.destination.y,
            "distance": sections[1].distance,
            "duration": sections[1].duration,
            "type": "end",
            "usn": data.usn
        }
    ]

}