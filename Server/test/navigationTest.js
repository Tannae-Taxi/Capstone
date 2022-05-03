let request = require('request');

let jsonData = {
    "origin": {
        "name": "Vehicle",
        "x": 127.11024293202674,
        "y": 37.394348634049784
    },
    "destination": {
        "name": "Destination",
        "x": 127.10860518470294,
        "y": 37.401999820065534
    },
    "waypoints": [
        {
            "name": "waypoint",
            "x": 127.11341936045922,
            "y": 37.39639094915999
        },
    ],
    "priority": "RECOMMEND",
    "car_fuel": "GASOLINE",
    "car_hipass": false,
    "alternatives": false,
    "road_details": false,
    "summary": true
}

let jsonReq = {
    headers: {
        'content-type': 'application/json',
        'authorization': 'KakaoAK d94b5c67305d6a10b3e43e5da881e7cf'
    },
    url: 'https://apis-navi.kakaomobility.com/v1/waypoints/directions',
    body: jsonData,
    json: true
}

request.post(jsonReq, (err, httpResponse, body) => {
    console.log(body.routes[0]);
})