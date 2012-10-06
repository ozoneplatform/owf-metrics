/**
 * @ignore
 */
var Ozone = Ozone || {};
Ozone.metrics = Ozone.metrics || {};
Ozone.metrics.util = Ozone.metrics.util || {};
Ozone.metrics.config = Ozone.metrics.config || {};

/**
 * @private
 * 
 * @description This method is useful if you want to see if a specfic url is the same
 * as the local url.  Can be used to test if AJAX can be used.  If url
 * is not a URL, it's assumed to be a relative filename, so this function
 * returns true.
 *
 * @param {String} url String that you want to check to see if its local.
 *
 * @returns Boolean
 *
 * @throws "Not a valid URL" if the parameter is not a valid url
 */
Ozone.metrics.util.isUrlLocal = function(url) {

    var webContextPath = Ozone.metrics.util.contextPath();

    //append last '/' this value should never be null
    if (webContextPath != '' && webContextPath != null) {
        webContextPath += '/';
    }

    //this regex matches urls against the configured webcontext path https://<contextPath>/.....
    //only one match is possible since this regex matches from the start of the string
    var regex = new RegExp('^https?:\/\/[^\/]+'+webContextPath);
    var server = url.match(regex);
    var local = window.location.toString().match(regex);

    //check if this might be a relative url 
    if (!server) {
        if (url.match(new RegExp('^https?:\/\/'))) {
            return false;
        }
        else {
            return true;
        }
    }

    //check if the first regex match of the server and local url are equal
    //if they are equal then the url is local
    //if not or the local url did not have any matches from the regex than return false
    return (local != null && local.length > 0) ? server[0] == local[0] : false;
};

/**
 * @private
 *
 * @description This method returns the current context path by
 * calling a controller at the server (will not
 * make the call if it has already been done).
 * 
 * @param {Object} o unused
 *
 * @returns context path with leading slash
 *          (ex. "/metric")
 *
 * @requires Ext base, dojo
 */
Ozone.metrics.util.contextPath = function(o) {

    if (Ozone.metrics.config.webContextPath == null) {
        //check window name for the property
        var configParams = Ozone.util.parseWindowNameData();
        if (configParams != null) {
            // launchConfig
            if (configParams.webContextPath != null) {
            	Ozone.metrics.config.webContextPath = configParams.webContextPath;
            }
        }
    }

    // return empty string if a valid context path wasn't found on window.name
    return Ozone.metrics.config.webContextPath || '';
};

/**
 * @private
 * 
 * @description Checks whether the url context is local or not,
 * then returns the valid url w/ context.
 * 
 * @param {String} value the url
 * @param {String} validContext valid context
 *
 * @returns valid url w/context if necessary, if not local then the url
 */
Ozone.metrics.util.validUrlContext = function(value, validContext){
    var containsRelDotPath = (value.indexOf("../") == 0)?true:false;
    var containsRelPath = (value.indexOf("/") == 0)?true:false;
    var isLocalUrl = (containsRelPath || containsRelDotPath || (value.indexOf("localhost") == 7) || (value.indexOf("localhost") == 11) || (value.indexOf("127.0.0.1") == 7)) ? true : false;
    var urlValidContext = value;
    validContext = ((validContext == undefined) ? Ozone.metrics.util.contextPath() : validContext);
    if((isLocalUrl == true) && (validContext != null)){
        if(containsRelPath){
            urlValidContext = String.format("{0}{1}", validContext, value);
        }else if(containsRelDotPath){
            var valuePathNoRel = value.substring(3);
            urlValidContext = String.format("{0}/{1}", validContext, valuePathNoRel);
        }
    }
    return urlValidContext;
};

/**
 * @private
 *
 * @description This method centralizes the container relay file for
 * RPC calls.
 *
 * @returns relay file path with context
 */
Ozone.metrics.util.getContainerRelay = function() {
    return Ozone.metrics.util.contextPath() + 
    '/js/eventing/rpc_relay.uncompressed.html';
};
