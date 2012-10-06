Ext.define('Ozone.components.admin.MetricsPanel', {
	  extend: 'Ext.panel.Panel',
  alias: ['widget.metricspanel', 'widget.Ozone.components.admin.MetricsPanel'],

  channel: 'AdminChannel',
  defaultTitle: 'Metrics',

  initComponent: function() {
	    
    var colorSchemes = {
    	"accessibility-wob": {
    		theme: "accessibility-wob",
    		background: {
    			fill: '#2A2A2A'
    		}
    	}
    };
	    
    // Set default theme
    if (!colorSchemes[Ozone.metrics.config.currentTheme.themeName]) {
    	colorSchemes[Ozone.metrics.config.currentTheme.themeName] = {
    		theme: "Base",
    		background: null
    	};
    }
    
  	var dateObj = new Date();
	var year = dateObj.getFullYear();
	var month = dateObj.getMonth();
	var day = dateObj.getDate();
	var fromDate = (new Date(year - 1, month, day));
	var toDate = new Date(year, month, day, 23, 59, 59, 999);  
		
    Ext.apply(this, {
    	itemId: 'main',
    	layout: 'border',
    	cls: 'metricspanel',
    	padding: 0,
    	items: [{
	        xtype: 'tabpanel',
	        itemId: 'graphPanel',
	        cls: 'metrics-graph-tab-panel',
	        height: 225,
	        region: 'center',
	        tbar: {
		      	padding: '2px',
		      	items: ['->', {
		  	        xtype: 'datefield',
		  	        fieldLabel: 'From',
		  	        cls: 'fromDate',
		  	        itemId: 'from_date',
		  	        editable: false,
		  	        value: fromDate,
		  	        maxValue: toDate,  // limited to the current date or prior
		  	        listeners: {
		  	            change: function(field, newValue, oldValue) {
		  	            	var metricspanel = field.up('metricspanel');
		  	                var grid = metricspanel.down("#gridPanel");
		  	            	var gridStore = grid.getStore();
		  	            	var componentId = "";
		  	            	gridStore.getProxy().extraParams.fromDate = newValue.getTime();
		  	            	gridStore.loadPage(1,{
			  	                params: {
			  	                  offset: 0,
			  	                  max: grid.pageSize
			  	                },
				  	          	callback: function(records, operation, success) {
			                      	// Update graph
			                      	var panel = metricspanel.down("#graphPanel");
			                      	if (panel.getActiveTab().itemId == "metricsgraphTab") {
			                      		var graph = panel.down('metricsgraph');
			                              if(graph){
			                              	var proxy = grid.getStore().getProxy();
			                                  graph.updateStore({
						                            fromDate: proxy.extraParams.fromDate, 
						                            toDate: proxy.extraParams.toDate,
						                            component: proxy.extraParams.component,
						                            componentId: componentId
					                          });
			                              }
			                      	}	                        			                        	
			  	            		grid.getSelectionModel().select(0);	    
			  	            	}
		  	            	});
		  	            }
		  	        }
		  	    }, {
		  	        xtype: 'datefield',
		  	        fieldLabel: 'To',
		  	        cls: 'toDate',
		  	        itemId: 'to_date',
		  	        editable: false,
		  	        value: toDate,  // defaults to today
		  	        maxValue: toDate,  // limited to the current date or prior
		  	        listeners: {
		  	            change: function(field, newValue, oldValue) {
		  	            	// Change toDate to last millisecond of selected date because date picker will 
		  	            	// not consider time so the full day won't be included in the totals.
		  	            	var year = newValue.getFullYear();
		  	            	var month = newValue.getMonth();
		  	            	var day = newValue.getDate();
		  	            	var dateObj = new Date(year, month, day, 23, 59, 59, 999);
		
		  	            	var metricspanel = field.up('metricspanel');
		  	                var grid = metricspanel.down("#gridPanel");
		  	            	var gridStore = grid.getStore();
		  	            	var componentId = "";
		  	            	gridStore.getProxy().extraParams.toDate = dateObj.getTime();
		  	            	gridStore.loadPage(1,{
			  	                params: {
			  	                  offset: 0,
			  	                  max: grid.pageSize
			  	                },
				  	          	callback: function(records, operation, success) {
			                      	// Update graph
			                      	var panel = metricspanel.down("#graphPanel");
			                      	if (panel.getActiveTab().itemId == "metricsgraphTab") {
			                      		var graph = panel.down('metricsgraph');
			                              if(graph){
			                              	var proxy = grid.getStore().getProxy();
			                                  graph.updateStore({
						                            fromDate: proxy.extraParams.fromDate, 
						                            toDate: proxy.extraParams.toDate,
						                            component: proxy.extraParams.component,
						                            componentId: componentId
					                          });
			                              }
			                      	}	                        			                        	
			  	            		grid.getSelectionModel().select(0);	  
				  	          	}
		  	            	});
		  	            }
		  	        }
		    	}]
	        },
	        items: [{
	          	title: 'Tag Cloud',
	          	itemId: 'tagCloudTab',
	          	items: [
	                {
	    			xtype: 'tagcloud',
	    			itemId: 'tagCloud',
	    			height: "100%",
	    			autoScroll: true
	    		}],
	    		listeners: {
	    			beforeactivate: function(tab) {
	                    var panel = tab.up("metricspanel");
	                    var grid = panel.down("#gridPanel");
	                    grid.hide();
	    			}
	    		}
	        },
	        {
	    		title: 'Graph',
	    		itemId: 'metricsgraphTab',
	            layout: {
	                type: 'vbox',
	                align: 'stretch'
	            },
	          	items: [
	                {
	                    xtype: 'panel',
	                    itemId: 'graphTitle',
	                    cls: 'graphTitle',
	                    autoScroll: true,
	                    html: "<center>Please select a widget from the list below to see the metrics.</center>"
	                },
	                {
	                    xtype: 'panel',
	                    layout: 'fit',
	                    flex: 1,
	                    border: false,
	                    items: [
	                      {
	                          xtype: 'metricsgraph',
	                          cls: 'metricsgraph',
	                          itemId: 'graph',
	                          theme: colorSchemes[Ozone.metrics.config.currentTheme.themeName].theme || 'Base',
	                          background: colorSchemes[Ozone.metrics.config.currentTheme.themeName].background || null
	                      }
	                    ]
	                }
	            ],
	    		listeners: {
	    			activate: function(tab) {
	                    var panel = tab.up("metricspanel");
	                    var grid = panel.down("#gridPanel");
	                    grid.show();
	                	var selection = grid.getSelectionModel().getSelection();
	                	if (grid.getStore().getRange().length > 0 && selection.length == 0) {
	                		grid.getSelectionModel().select(0);
	                	}
	    			}
	    		}
	        }]
    	},
    	{
    		xtype: 'metricsgrid',
    		itemId: 'gridPanel',
    		region: 'south',
    		split: true,
    		hidden: true
    	}]
    });

    this.callParent();
  }



});