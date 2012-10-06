Ext.define('Ozone.components.admin.MetricsGrid', {
    extend: 'Ext.grid.Panel',
    alias: ['widget.metricsgrid'],
    
    height: 220,
    width: 581,
    pageSize: 50,

    initComponent: function(){

    	var dateObj = new Date();
    	var year = dateObj.getFullYear();
    	var month = dateObj.getMonth();
    	var day = dateObj.getDate();
    	var fromDate = (new Date(year - 1, month, day));
    	var toDate = new Date(year, month, day, 23, 59, 59, 999);
        var gridStore = Ext.create('Ext.data.Store', {
            pageSize: this.pageSize,
            remoteSort: true,
            sorters: [
              {
                property : 'component',
                direction: 'ASC'
              }
            ],
            proxy: {
                url: Ozone.metrics.util.contextPath() + '/metricView',
                limitParam: 'max',
                pageParam: undefined,
                startParam: 'offset',
                simpleSortMode: true,
                sortParam: 'sort',
                directionParam: 'order',
                extraParams: {
                    fromDate: fromDate.getTime(),
                    toDate: toDate.getTime()
                },
                type: 'ajax',
                reader: {
                    type: 'json',
                    root: 'results',
                    totalProperty: 'total'
                }
            },
            fields: ['component', 'componentId', 'views', 'users'],
            idIndex: 0,
            listeners: {
            	load: {
            		fn: function(store, records, successful, operation) {
	                    var main = this.up("#main");
            			if (successful) {
		                    var fromDate = main.down("#from_date").getValue();
		                    var toDate = main.down("#to_date").getValue();
		                    var component = main.down("#searchfield").getValue();
		  	                var grid = main.down("#gridPanel");
		                    main.down("tagcloud").generateTagCloud({
		                    	fromDate: fromDate.getTime(), 
		                    	toDate: toDate.getTime(),
		                    	component: component
		                    });
		                    if (grid.isVisible() && records && records.length > 0) {
			  	                grid.getSelectionModel().select(0);
		                    }
            			} else {
            				Ozone.Msg.alert("Error", "Error retrieving data");
//            				main.down("#searchfield").onClear();
            			}
            		},
            		scope: this
            	}
            }
        });

        Ext.apply(this, {
            store: gridStore,
            columns: [
                {
                    header: 'Id',
                    dataIndex: 'componentId',
                    hidden: true,
                    flex: 3
                }, {
                    header: 'Name',
                    dataIndex: 'component',
                    renderer: function(value){
                    	return Ozone.util.HTMLEncode(value);
                    },
                    flex: 3
                }, {
                    header: 'Views',
                    dataIndex: 'views',
                    flex: 1
                }, {
                    header: 'Users',
                    dataIndex: 'users',
                    flex: 1
                }
            ],
	        tbar: {
		      	padding: '2px',
		      	items: ['->', {
		            xtype: 'searchbox',
		  	        itemId: 'searchfield',
		            listeners: {
		                searchChanged: function (cmp, value) {
	                      	// Update graph
		  	            	var metricspanel = cmp.up('metricspanel');
		  	                var grid = metricspanel.down("#gridPanel");
	                      	var panel = metricspanel.down("#graphPanel");
		  	            	var gridStore = grid.getStore();
		  	            	gridStore.getProxy().extraParams.component = value;
		  	            	gridStore.getProxy().extraParams.componentId = null;
		  	            	gridStore.loadPage(1,{
			  	                params: {
			  	                  offset: 0,
			  	                  max: grid.pageSize
			  	                }
		  	            	});
		                }
		            }
		    	}]
	        },
            dockedItems: [Ext.create('Ext.PagingToolbar', {
            	itemId: 'pagingToolbar',
                store: gridStore,
                dock: 'bottom',
          	  	padding: '2px',
                displayInfo: true,
                displayMsg: 'Displaying widgets {0} - {1} of {2}',
                emptyMsg: "No widgets to display"
            })],
            listeners: {
                selectionchange: {
                    fn: function(selectionModel, records){
                        var panel = this.up('metricspanel');
                        var graphPanel = panel.down('#graphPanel');
                        var graph = panel.down('metricsgraph');
                        var metricsgraphTab = panel.down('#metricsgraphTab');
	                    var graphTitle = "<center>No results found. Please modify your search criteria.</center>";
                    	if (records[0] && records[0].data) {
                    		graph.show();
	                        var componentId = records[0].data.componentId;
	                        
	                        // Hide No Results Message
	                        graphTitle = "<center>" + Ozone.util.HTMLEncode(records[0].data.component) + "</center>";
	                        
	                        // Update Graph
	                        if(graph){
	                        	var proxy = this.getStore().getProxy();
	                            graph.updateStore({
		                            fromDate: proxy.extraParams.fromDate, 
		                            toDate: proxy.extraParams.toDate,
		                            component: proxy.extraParams.component,
		                            componentId: componentId
	                            });
	                        }
                    	} else {
                    		graph.hide();
                    	}
                        metricsgraphTab.down('#graphTitle').update(graphTitle);
                    },
                    scope: this
                }
            }
        });

        gridStore.load({
        	start: 0, 
        	limit: this.pageSize
        });

        this.callParent();
    },
    
    load: function() {
        this.store.loadPage(1);
    },

    refresh: function() {
      this.store.loadPage(this.store.currentPage);
    }

});