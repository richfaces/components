(function($, rf) {
    rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};

    var defaultOptions = {
        positionType : "DROPDOWN",
        direction : "AA",
        jointPoint : "AA",
        cssRoot : "ddm",
        cssClasses : {}
    };

    // constructor definition
    rf.rf4.ui.Menu = function(componentId, options) {
        this.options = {};
        $.extend(this.options, defaultOptions, options || {});
        $.extend(this.options.cssClasses, buildCssClasses.call(this, this.options.cssRoot));
        $super.constructor.call(this, componentId, this.options);
        this.id = componentId;
        this.namespace = this.namespace || "." + rf.Event.createNamespace(this.name, this.id);
        this.groupList = new Array();

        this.target = this.getTarget();
        if (this.target) {
            var menu = this;
            $(document).ready(function() {
                var targetComponent = RichFaces.component(menu.target);
                if (targetComponent && targetComponent.contextMenuAttach) {
                    targetComponent.contextMenuAttach(menu);
                } else {
                    rf.Event.bindById(menu.target, menu.options.showEvent, $.proxy(menu.__showHandler, menu), menu)
                }
            });
        }
        this.element = $(rf.getDomElement(this.id));

        if (!rf.rf4.ui.MenuManager) {
            rf.rf4.ui.MenuManager = {};
        }
        this.menuManager = rf.rf4.ui.MenuManager;
    };

    var buildCssClasses = function(cssRoot) {
        var cssClasses = {
            selectMenuCss : "rf-" +cssRoot+ "-sel",
            unselectMenuCss : "rf-" +cssRoot+ "-unsel"
        }
        return cssClasses;
    }

    rf.rf4.ui.MenuBase.extend(rf.rf4.ui.Menu);

    // define super class link
    var $super = rf.rf4.ui.Menu.$super;

    $.extend(rf.rf4.ui.Menu.prototype, rf.rf4.ui.MenuKeyNavigation);

    $.extend(rf.rf4.ui.Menu.prototype, (function() {
        return {
            name : "Menu",
            initiateGroups : function(groupOptions) {
                for (var i in groupOptions) {
                    var groupId = groupOptions[i].id;
                    if (null != groupId) {
                        this.groupList[groupId] = new RichFaces.rf4.ui.MenuGroup(
                            groupId, {
                                rootMenuId : this.id,
                                onshow : groupOptions[i].onshow,
                                onhide : groupOptions[i].onhide,
                                horizontalOffset: groupOptions[i].horizontalOffset,
                                verticalOffset: groupOptions[i].verticalOffset,
                                jointPoint : groupOptions[i].jointPoint,
                                direction : groupOptions[i].direction,
                                cssRoot : groupOptions[i].cssRoot
                            });
                    }
                }
            },

            getTarget : function() {
                return this.id + "_label";
            },

            show : function(e) {
                if (this.menuManager.openedMenu != this.id) {
                    this.menuManager.shutdownMenu();
                    this.menuManager.addMenuId(this.id);
                    this.__showPopup();
                }
            },

            hide : function() {
                this.__hidePopup();
                this.menuManager.deletedMenuId();
            },

            select : function() {
                this.element.removeClass(this.options.cssClasses.unselectMenuCss);
                this.element.addClass(this.options.cssClasses.selectMenuCss);
            },
            unselect : function() {
                this.element.removeClass(this.options.cssClasses.selectMenuCss);
                this.element.addClass(this.options.cssClasses.unselectMenuCss);
            },

            __overHandler : function() {
                $super.__overHandler.call(this);
                this.select();
            },

            __leaveHandler : function() {
                $super.__leaveHandler.call(this);
                this.unselect();
            },

            destroy : function() {
                // clean up code here
                this.detach(this.id);

                if (this.target) {
                    rf.Event.unbindById(this.target, this.options.showEvent);
                }

                // call parent's destroy method
                $super.destroy.call(this);

            }
        };
    })());

    rf.rf4.ui.MenuManager = {
        openedMenu : null,

        activeSubMenu : null,

        addMenuId : function(menuId) {
            this.openedMenu = menuId;
        },

        deletedMenuId : function() {
            this.openedMenu = null;
        },

        shutdownMenu : function() {
            if (this.openedMenu != null) {
                rf.component(rf.getDomElement(this.openedMenu)).hide();
            }
            this.deletedMenuId();
        },

        setActiveSubMenu : function(submenu) {
            this.activeSubMenu = submenu;
        },

        getActiveSubMenu : function() {
            return this.activeSubMenu;
        }
    }
})(RichFaces.jQuery, RichFaces);