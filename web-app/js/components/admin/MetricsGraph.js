//extjs
Ext.define('Ozone.components.admin.MetricsGraph', {
    extend: 'Ext.chart.Chart',
    alias: ['widget.metricsgraph', 'widget.Ozone.components.admin.MetricsGraph'],

    width: 790,
    height: 200,
    animate: {
        easing: 'easeIn'
    },
    store: null,
    componentId: null,

    initComponent: function(){        

    	var dateObj = new Date();
    	var year = dateObj.getFullYear();
    	var month = dateObj.getMonth();
    	var day = dateObj.getDate();
        var gridStore = Ext.create('Ext.data.ArrayStore', {
            proxy: { 
                url: Ozone.metrics.util.contextPath() + '/metricGraphData',
                extraParams: {
                    componentId: this.componentId,
                    fromDate: (new Date(year - 1, month, day)).getTime(),
                    toDate: (dateObj).getTime()
                },
                type: 'ajax',
                reader: {
                    type: 'array'
                }
            },
			fields: [{name: 'year', type: 'long'}, {name: 'month', type: 'String'}, {name: 'views', type: 'long'}],
            idIndex: 0
        });

        gridStore.load();

        Ext.apply(this, {
            title: 'Widget Views',
            store: gridStore,

            axes: [
                {
                    type: 'Numeric',
                    position: 'left',
                    fields: ['views'],
                    title: 'Number of Views',
                    minimum: 0,
                    minorTickSteps: 4
                },
                {
                    type: 'Numeric',
                    position: 'right',
                    fields: ['views'],
                    title: 'Number of Views',
                    minimum: 0,
                    minorTickSteps: 4
                },
                {
                    type: 'Category',
                    position: 'bottom',
                    fields: ['month']
                }
            ],
            series: [{
                type: 'column',
                axis: ['left'],
                xField: 'month',
                yField: 'views',
                tips: {
                    trackMouse: true,
                    width: 140,
                    height: 60,
                    constrainPosition: true,
                    renderer: function (storeItem, item) {
                        month = storeItem.get('month');
						year = storeItem.get('year');
                        this.setTitle(month + ' ' + year + ': ' + storeItem.get(item.series.yField) + ' views');

                    }
              }

            }]

        });

        this.callParent();
    },


    updateStore: function(params){
    	var fromDate = params.fromDate;
    	var toDate = params.toDate;
        this.componentId = params.componentId;

    	var dateObj = new Date();
        if (toDate == null) {
        	toDate = new Date();
        } else {
        	toDate = new Date(toDate);
        }
        
    	// Change toDate to last millisecond of selected date because date picker will 
    	// not consider time so the full day won't be included in the totals.
    	var year = toDate.getFullYear();
    	var month = toDate.getMonth();
    	var day = toDate.getDate();
    	toDate = new Date(year, month, day, 23, 59, 59, 999);

    	if (fromDate == null) {
   			// fromDate defaults to one year from toDate
   			fromDate = new Date(year - 1, month, day);
    	} else {
    		fromDate = new Date(fromDate);
    	}
    	
        var proxy = this.store.getProxy();
        proxy.extraParams = {
            componentId: this.componentId,
            fromDate: fromDate.getTime(),
            toDate: toDate.getTime()
        };

        this.store.setProxy(proxy);
        this.store.load({
            scope   : this,
            callback: function(records, operation, success) {
                // Calculate ticks based on highest number of views for a single widget
                var highestCount = 0;
                for (var i = 0; i < records.length; i++) {
                	var viewCount = records[i].data.views;
                	if (viewCount > highestCount) highestCount = viewCount;
                }
                var remainder = highestCount % 5;
                var majorTickSteps = ((highestCount - remainder) / 5);
                var maximum = 5 * majorTickSteps;
                if (remainder !== 0) maximum += 5;
                if (remainder == 0) majorTickSteps -= 1;
                if (majorTickSteps < 0) majorTickSteps = 0;
                
                this.axes.getAt(0).majorTickSteps = majorTickSteps;
                this.axes.getAt(1).majorTickSteps = majorTickSteps;
                this.axes.getAt(0).maximum = maximum;
                this.axes.getAt(1).maximum = maximum;
                
                this.redraw();
            }
        });        
    }
});