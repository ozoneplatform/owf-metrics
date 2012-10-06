<%@ page contentType="text/html; UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title id='title'>OWF Metrics</title>
		
		<!-- ** CSS ** -->
        <!-- base library -->
        <g:if test="${params.themeName != null && params.themeName != ''} ">
        <link rel='stylesheet' type='text/css' href='../themes/${params.themeName}.theme/css/${params.themeName}.css' />
        </g:if>
        <g:else>
        <p:css name='../${owfCss.defaultCssPath()}' absolute='true'/>
        </g:else>

        <!-- initialize ozone configuration from server -->
        <owfImport:jsOwf path="config" resource="config" />

        <!-- include our server bundle, in dev mode a full list of js includes will appear -->
        <p:javascript src='ozone-metric-widget'/>
        <!-- include our server bundle, in dev mode a full list of js includes will appear -->

		<script language="javascript">
            if (Ext.isIE) {
              Ext.BLANK_IMAGE_URL = './themes/owf-ext-theme/resources/themes/images/owf-ext/s.gif';
            }
            owfdojo.config.dojoBlankHtmlUrl =  '../js-lib/dojo-1.5.0-windowname-only/dojo/resources/blank.html';

             //The location is assumed to be at /<context>/js/eventing/rpc_relay.uncompressed.html if it is not set
            Ozone.eventing.Widget.widgetRelayURL = Ozone.util.contextPath() + '/js/eventing/rpc_relay.uncompressed.html';

            Ext.onReady(function(){
            	var windowNameData = Ozone.util.parseWindowNameData(window.name);
				Ozone.metrics.config.currentTheme = windowNameData ? windowNameData.currentTheme : ('${params.themeName}' ? { themeName: '${params.themeName}' } : { themeName: 'a_default' });
				
                var viewport = Ext.create('Ext.container.Viewport', {
                    layout: 'fit',
                    id: 'viewport',
                    isLayedOut: false,
                    listeners: {
                        afterlayout: {
                            fn: function(cmp) {
                                cmp.isLayedOut = true;
                            },
                            single: true
                        }
                    },
                    items: [
                        {
                            xtype: 'metricspanel',
                            itemId: 'metricspanel'
                        }
                    ]
                });
 
            });

            function graph(componentId, component){
                var panel = Ext.getCmp('viewport').down('metricspanel');
                var graphPanel = Ext.getCmp('viewport').down('#graphPanel');
                var graph = Ext.getCmp('viewport').down('metricsgraph');
                var grid = Ext.getCmp('viewport').down('#gridPanel');
                
        		// Deselect grid selection (if any)
                grid.getSelectionModel().deselectAll();
                
                // Hide No Results Message
                var metricsgraphTab = panel.down('#metricsgraphTab');
                var graphTitle = "<center>" + Ozone.util.HTMLEncode(component) + "</center>";
                metricsgraphTab.down('#graphTitle').update(graphTitle);
                
                // Update Graph
                if(graph){
                	var searchfield = Ext.getCmp('viewport').down('#searchfield');
                	searchfield.setValue(component);
                    var gridStore = grid.getStore();
	                var proxy = grid.getStore().getProxy();
  	            	proxy.extraParams.componentId = componentId;
  	            	proxy.extraParams.component = component;
  	            	gridStore.loadPage(1,{
	  	                params: {
	  	                  offset: 0,
	  	                  max: grid.pageSize
	  	                },
  	            		callback: function(records, operation, success) {
			                // Activate Graph tab
			                graphPanel.setActiveTab(1);
	                
	                        graph.updateStore({
	                            fromDate: proxy.extraParams.fromDate, 
	                            toDate: proxy.extraParams.toDate,
	                            component: proxy.extraParams.component,
	                            componentId: proxy.extraParams.componentId
	                        });
                        }
  	            	});
                }
            }
		</script>
    </head>

    <body>
    </body>
</html>