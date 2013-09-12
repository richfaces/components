/**
 * @author Ilya Shaikovsky
 * @author Lukas Fryc
 */

(function($, rf) {

    rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};

    var defaultOptions = {
        enabledInInput : false,
        preventDefault : true
    };
    
    var types = [ 'keydown', 'keyup' ];

    rf.rf4.ui.HotKey = function(componentId, options) {
        $super.constructor.call(this, componentId);
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.id);
        this.attachToDom(this.componentId);
        this.options = $.extend({}, defaultOptions, options);
        this.__handlers = {};
        
        this.options.selector = (this.options.selector) ? this.options.selector : document;

        $(document).ready($.proxy(function() {
            this.__bindDefinedHandlers();
        }, this));
    };

    rf.BaseComponent.extend(rf.rf4.ui.HotKey);

    var $super = rf.rf4.ui.HotKey.$super;

    $.extend(rf.rf4.ui.HotKey.prototype, {

        name : "HotKey",
        
        __bindDefinedHandlers : function() {
            for (var i = 0; i < types.length; i++) {
                if (this.options['on' + types[i]]) {
                    this.__bindHandler(types[i]);
                }
            }
        },
        
        __bindHandler : function(type) {
            this.__handlers[type] = $.proxy(function(event) {
                var result = this.invokeEvent.call(this, type, document.getElementById(this.id), event);
                if (this.options.preventDefault) {
                    event.stopPropagation();
                    event.preventDefault();
                    return false;
                }
                return result;
            }, this);
            $(this.options.selector).bind(type + this.namespace, this.options, this.__handlers[type]);
        },

        destroy : function() {
            rf.Event.unbindById(this.id, this.namespace);

            for (var type in this.__handlers) {
                if (this.__handlers.hasOwnProperty(type)) {
                    $(this.options.selector).unbind(type + this.namespace, this.__handlers[type]);
                }
            }

            $super.destroy.call(this);
        }
    });

})(RichFaces.jQuery, RichFaces);