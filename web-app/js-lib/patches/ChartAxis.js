Ext.override(Ext.chart.axis.Axis, {

    drawHorizontalLabels: function() {
       var  me = this,
            labelConf = me.label,
            floor = Math.floor,
            max = Math.max,
            axes = me.chart.axes,
            position = me.position,
            inflections = me.inflections,
            ln = inflections.length,
            labels = me.labels,
            labelGroup = me.labelGroup,
            maxHeight = 0,
            ratio,
            gutterY = me.chart.maxGutter[1],
            ubbox, bbox, point, prevX, prevLabel,
            projectedWidth = 0,
            textLabel, attr, textRight, text,
            label, last, x, y, i, firstLabel;

        last = ln - 1;
        
        point = inflections[0];
        firstLabel = me.getOrCreateLabel(0, me.label.renderer(labels[0]));
        ratio = Math.floor(Math.abs(Math.sin(labelConf.rotate && (labelConf.rotate.degrees * Math.PI / 180) || 0)));

        for (i = 0; i < ln; i++) {
            point = inflections[i];
            text = me.label.renderer(labels[i]);
            textLabel = me.getOrCreateLabel(i, text);
            bbox = textLabel._bbox;
            maxHeight = max(maxHeight, bbox.height + me.dashSize + me.label.padding);
            x = floor(point[0] - (ratio? bbox.height : bbox.width) / 2);
            if (me.chart.maxGutter[0] == 0) {
                if (i == 0 && axes.findIndex('position', 'left') == -1) {
                    x = point[0];
                }
                else if (i == last && axes.findIndex('position', 'right') == -1) {
                    x = point[0] - bbox.width;
                }
            }
            if (position == 'top') {
                y = point[1] - (me.dashSize * 2) - me.label.padding - (bbox.height / 2);
            }
            else {
                y = point[1] + (me.dashSize * 2) + me.label.padding + (bbox.height / 2);
            }

            textLabel.setAttributes({
                hidden: false,
                x: x,
                y: y
            }, true);

            /*
            if (i != 0 && (me.intersect(textLabel, prevLabel)
                || me.intersect(textLabel, firstLabel))) {
                textLabel.hide(true);
                continue;
            }
            */

            prevLabel = textLabel;
        }

        return maxHeight;
    }

});