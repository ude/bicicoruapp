'use strict';

const express = require('express');
const Promise = require('bluebird');
const request = require('request');
const xml2js = require('xml2js')
const fs = require("fs");


const app = express();
const parser = new xml2js.Parser({
  explicitArray : false,
  ignoreAttrs: true,
});

/**
 * Stations memory cache
 */
const stations = {
  data: {
    EstacionAdditionalInformationDto: [],
  },
  lastUpdate: null,
  errors: [],
};

/**
 * Get stations
 * @return {Object} stations
 */
const getStations = (fromCache = true) => {
  return new Promise((resolve, reject) => {
    if (fromCache && stations.data.EstacionAdditionalInformationDto.length > 0) {
      return resolve(stations.data);
    }

		const headers = {
			'Content-Type': 'text/xml; charset=utf-8',
			soapaction: 'http://aparcabicis.nextgal.es/GetEstaciones'
		};

		const payload = `<?xml version="1.0" encoding="utf-8"?><soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body><GetEstaciones xmlns="https://bicicoruna.es/" /></soap:Body></soap:Envelope>`;

    request({ 
      url: 'https://www.bicicoruna.es//swm/WSMobile.asmx',
      method: 'POST',
      body: payload,
      headers,
    }, (err, response, body) => {
      const now = new Date().toISOString().substring(0, 19);
      stations.lastUpdate = now;

      if(err) {
        stations.errors.push(e.message);
        return resolve(stations.data);
      }
  
      parser.parseString(body, (err, dataParsed) => {
        if(err) {
          stations.errors.push(e.message);
          return resolve(stations.data);
        }
  
        try {
          const data = dataParsed["soap:Envelope"]["soap:Body"]["GetEstacionesResponse"]["GetEstacionesResult"];
          return resolve(data);
        } catch(e) {
          stations.errors.push(e.message);
          return reject(new Error(`unexpected format: ${e}`));
        }
      });
    });
  });
};

/**
 * Keep statios cached by X minutes (time in minutes)
 */
const TTL_CACHE_STATIONS = 1;
const content = fs.readFileSync('./applicense.html', 'utf-8');
const test = fs.readFileSync('./data/munrotab_v6.2.csv', 'utf-8')

const cacheStations = (fromCache = true) => {
  getStations(fromCache).then((stationsInfo) => {
    stations.data.EstacionAdditionalInformationDto = Array.from(stationsInfo.EstacionAdditionalInformationDto);
  }).catch((err) => {
    console.error(err);
  });
};

setInterval(() => {
  cacheStations(false);
}, TTL_CACHE_STATIONS * 60 * 1000);

cacheStations(false);

app.get('/', (req, res) => {
  res.send('Bicoruna api');
});

app.get('/debug/ude', (req, res) => {
  res.send({
    lastUpdate: stations.lastUpdate,
    errors: stations.errors,
  })
});

app.get('/stations', (req, res) => {
	getStations().then((data) => {
    res.send(data);
  }).catch((err) => {
    res.status(500).send(err.message);
    console.error(err);
  });
});

app.get('/station/:id', (req, res) => {
	res.status(402).send('not implemented');
})

app.get('/license', (req, res) => {
  res.send(content);
  });


app.get('/test', (req, res) => {
  res.send(test);
  });

const port = process.env.PORT || 3000;
app.listen(port, '0.0.0.0',  () => {
  console.log(`Example app listening on port ${port}`);
});