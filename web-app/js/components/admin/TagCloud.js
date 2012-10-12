Ext.define('Ozone.components.admin.TagCloud', {
    extend: 'Ext.panel.Panel',
    alias: ['widget.tagcloud', 'widget.Ozone.components.admin.TagCloud'],

    generateTagCloud: function(params) {
        params = params || {};
        var fromDate = params.fromDate;
        var toDate = params.toDate;
        scope = this;
		
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
    	
        Ext.Ajax.request({
            url: Ozone.metrics.util.contextPath() + '/tagCloudMetric',
            params: {
                fromDate: fromDate.getTime(),
                toDate: toDate.getTime()
            },
            success: function(response){
                var data = Ext.decode(response.responseText).results;
                scope.update(data);
            }
        });
    },

    initComponent: function() {
        var me = this;
        me.tpl = new Ext.XTemplate(
            '<ul class="cloud">{[this.initialize(values)]}' +
                '<tpl for=".">' +
                    '<li>' +
                        '<span ' +
                            'id={componentId} ' +
                            'title="{total}" ' +
                            'class="tag" ' +
                            'style="font-size:{[this.fontSize(values)]}em" ' +
                            'onmouseover="document.getElementById(\'{componentId}\').className=\'tag-active\'" ' + 
                            'onmouseout="document.getElementById(\'{componentId}\').className=\'tag\'" ' +
                            'onclick = "graph(\'{componentId}\')" ' +
                        '>' +
                            '{[this.nameNoSpace(values)]} ' +
                        '</span>' +
                    '</li>' +
                '</tpl>' +
            '</ul>',
            {
                min: -1,
                minSize: 0,
                constant: 0,
                initialize: function(values) {
                    var instance;
                    var max = -1;
                    var min = -1;

                    //find max and min number of views
                    for (var i = 0; i < values.length; i++) {
                        instance = values[i];
                        var views = instance.total;
		
                        if (max == -1) {
                            max = views;
                        } else if (max < views) {
                            max = views;
                        }
		
                        if (min == -1) {
                            min = views;
                        } else if (min > views) {
                            min = views;
                        }
                    }

                    var maxSize = 2.5;
                    var minSize = 1.0;
                    var constant = Math.log(max - (min - 1)) / (maxSize - minSize)
                                
                    this.min = min;
                    this.minSize = minSize;
                    this.constant = constant;
                },
                nameNoSpace: function(values) {
                    var name = values.component.toLowerCase();
                    return Ozone.util.HTMLEncode(name.replace(/ /g, '_'));
                },
                fontSize: function(values) {
                    return Math.log(values.total - (this.min - 1)) / this.constant + this.minSize;
                }
            }
        );
        this.generateTagCloud();
        this.callParent();
    }
});