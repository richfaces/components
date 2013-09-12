(function ($, rf) {

    rf.rf4 = rf.rf4 || {};
    rf.rf4.ui = rf.rf4.ui || {};

    rf.rf4.ui.CollapsibleSubTableToggler = function(id, options) {
        this.id = id;
        this.eventName = options.eventName;
        this.expandedControl = options.expandedControl;
        this.collapsedControl = options.collapsedControl;
        this.forId = options.forId;
        this.element = $(document.getElementById(this.id));

        if (this.element && this.eventName) {
            this.element.bind(this.eventName, $.proxy(this.switchState, this));
        }
    };

    $.extend(rf.rf4.ui.CollapsibleSubTableToggler.prototype, (function () {

        var getElementById = function(id) {
            return $(document.getElementById(id))
        }

        return {

            switchState: function(e) {
                var subtable = rf.component(this.forId);
                if (subtable) {
                    var mode = subtable.getMode();

                    if (rf.rf4.ui.CollapsibleSubTable.MODE_CLNT == mode) {
                        this.toggleControl(subtable.isExpanded());
                    }

                    subtable.setOption(this.id);
                    subtable.switchState(e);
                }
            },

            toggleControl: function(collapse) {
                var expandedControl = getElementById(this.expandedControl);
                var collapsedControl = getElementById(this.collapsedControl);

                if (collapse) {
                    expandedControl.hide();
                    collapsedControl.show();
                } else {
                    collapsedControl.hide();
                    expandedControl.show();
                }
            }
        };
    })());

})(RichFaces.jQuery, window.RichFaces);