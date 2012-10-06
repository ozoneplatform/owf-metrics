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

				var instance;
				var max = -1;
				var min = -1;

				//find max and min number of views
				for(var i = 0; i < data.length; i++){
					instance = data[i];
					var views = instance.total;
		
					if(max == -1){
						max = views;
					}else if(max < views){
						max = views;
					}
		
					if(min == -1){
						min = views;
					}else if (min > views){
						min = views;
					}
				}

				var numFonts = 8;
				var delta = (max - min)/ numFonts;
				var intervals = [];
		
				for(var k = 1; k <= numFonts; k++){
					intervals[k - 1] = min + (k * delta);
				}
		
				var html = '<ul class ="cloud">';
				var tag = 'tag';
				var views;
				var maxSize = 2.5;
				var minSize = 1.0;
				var constant = Math.log(max - (min - 1)) / (maxSize - minSize)
		
				for(var j = 0; j < data.length; j++){
					instance = data[j];
					views = instance.total;
		
					var fontSize = Math.log(views - (min - 1)) / constant + minSize;
					var name = instance.component.toLowerCase();
					var nameNoSpace = Ozone.util.HTMLEncode(name.replace(/ /g, '_'));
                                        var nameSafe = nameNoSpace.replace(/\\/g, '__');
					var componentId = instance.componentId;
					var component = instance.component;
		
					// adds the widget name to the list with the views as a tooltip
					html += '<li><span id =' + nameSafe + ' title="' + instance.total + '" class="' + tag + '" style="font-size:' + fontSize + 'em" ' + 
					'onmouseover = "document.getElementById(\'' + nameSafe + '\').className=\'' + tag + '-active\'" ' + '" onmouseout = "document.getElementById(\'' + nameSafe + '\').className=\'' + tag + '\'"' +
					     ' onclick = "graph(\'' + componentId +'\',\'' + component + '\')" >' + nameNoSpace + ' ' + '</span></li>'
				}
		
				html += '</ul>'
				scope.update(html)	        
			}
		});
	},
	
	initComponent: function() {
		this.generateTagCloud();
		this.callParent();
	}
});