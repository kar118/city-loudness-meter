const heatmap = new ol.layer.Heatmap({
	source: new ol.source.Vector({
		url: '../api/location/all',
		format: new ol.format.GeoJSON()
	}),
	blur: 25,
	radius: 25,
	weight: (feature) => feature.get('magnitude') / 100
})

const tile = new ol.layer.Tile({
	source: new ol.source.OSM()
})

const map = new ol.Map({
	target: 'map',
	layers: [tile, heatmap],
	view: new ol.View({
		center: ol.proj.fromLonLat([19.945, 50.064]),
		zoom: 15
	})
})
