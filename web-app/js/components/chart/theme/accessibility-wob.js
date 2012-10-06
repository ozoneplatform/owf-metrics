Ext.define('Ext.chart.theme.accessibility-wob', {
    extend: 'Ext.chart.theme.Base',

    constructor: function(config) {
    	var baseColor = '#FFFFFF';
    	var colors = ['#DDF991'];
    	
        this.callParent([Ext.apply({
		    axis: {
		    	fill: baseColor,
		    	stroke: baseColor
		    },
		    axisLabelTop: {
		        fill: baseColor
		    },
		    axisLabelLeft: {
		        fill: baseColor
		    },
		    axisLabelRight: {
		        fill: baseColor
		    },
		    axisLabelBottom: {
		        fill: baseColor
		    },
		    axisTitleTop: {
		        fill: baseColor
		    },
		    axisTitleLeft: {
		        fill: baseColor
		    },
		    axisTitleRight: {
		        fill: baseColor
		    },
		    axisTitleBottom: {
		        fill: baseColor
		    },
            colors: colors
        }, config)]);
    }
});