(function($, rf) {
    rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};
    var defaultOptions = {
        showEvent : 'mouseenter',
        direction : "AA",
        jointPoint : "AA",
        positionType : "DDMENUGROUP",
        showDelay : 300
    }
    // constructor definition
    rf.rf4.ui.MenuGroup = function(componentId, options) {
        this.id = componentId;
        this.options = {};
        $.extend(this.options, defaultOptions, options || {});
        $super.constructor.call(this, componentId, this.options);
        this.namespace = this.namespace || "."
            + rf.Event.createNamespace(this.name, this.id);
        this.attachToDom(componentId);

        rf.Event.bindById(this.id, this.options.showEvent, $.proxy(
            this.__showHandler, this), this);

        this.rootMenu = rf.component(this.options.rootMenuId);

        this.shown = false;
        this.jqueryElement = $(this.element);

    };

    rf.rf4.ui.MenuBase.extend(rf.rf4.ui.MenuGroup);

    // define super class link
    var $super = rf.rf4.ui.MenuGroup.$super;

    $.extend(rf.rf4.ui.MenuGroup.prototype, rf.rf4.ui.MenuKeyNavigation);

    $.extend(rf.rf4.ui.MenuGroup.prototype, (function() {
        return {
            name : "MenuGroup",
            show : function() {
                var id = this.id;
                if (this.rootMenu.groupList[id] && !this.shown) {
                    this.rootMenu.invokeEvent("groupshow", rf
                        .getDomElement(this.rootMenu.id),
                        null);
                    this.__showPopup();
                    this.shown = true;
                }
            },
            hide : function() {
                var menu = this.rootMenu;
                if (menu.groupList[this.id] && this.shown) {
                    menu.invokeEvent("grouphide", rf
                        .getDomElement(menu.id), null);
                    this.__hidePopup();
                    this.shown = false;
                }
            },

            select : function() {
                this.jqueryElement.removeClass(this.options.cssClasses.unselectItemCss);
                this.jqueryElement.addClass(this.options.cssClasses.selectItemCss);
            },
            unselect : function() {
                this.jqueryElement.removeClass(this.options.cssClasses.selectItemCss);
                this.jqueryElement.addClass(this.options.cssClasses.unselectItemCss);
            },

            __showHandler : function() {
                this.select();
                $super.__showHandler.call(this);
            },
            __leaveHandler : function() {
                window.clearTimeout(this.showTimeoutId);
                this.showTimeoutId = null;
                this.hideTimeoutId = window.setTimeout($.proxy(
                    function() {
                        this.hide();
                    }, this), this.options.hideDelay);
                this.unselect();
            },

            destroy : function() {
                // clean up code here
                this.detach(this.id);
                // call parent's destroy method
                $super.destroy.call(this);
            }
        }

    })());
})(RichFaces.jQuery, RichFaces)