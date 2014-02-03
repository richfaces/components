(function ($, rf) {

    rf.ui = rf.ui || {};

    rf.ui.InplaceSelect = function(id, options) {
        var mergedOptions = $.extend({}, defaultOptions, options);
        $super.constructor.call(this, id, mergedOptions);
        this.getInput().bind("click", $.proxy(this.__clickHandler, this));
        mergedOptions['attachTo'] = id;
        mergedOptions['scrollContainer'] = $(document.getElementById(id + "Items")).parent()[0];
        mergedOptions['focusKeeperEnabled'] = false;
        this.popupList = new rf.ui.PopupList(id + "List", this, mergedOptions);
        this.list = this.popupList.__getList();
        this.clientSelectItems = mergedOptions.clientSelectItems;
        this.selValueInput = $(document.getElementById(id + "selValue"));
        this.initialValue = this.selValueInput.val();
        this.listHandler = $(document.getElementById(id + "List"));
        this.listHandler.bind("mousedown", $.proxy(this.__onListMouseDown, this));
        this.listHandler.bind("mouseup", $.proxy(this.__onListMouseUp, this));
        this.openOnEdit = mergedOptions.openOnEdit;
        this.saveOnSelect = mergedOptions.saveOnSelect;
        this.savedIndex = -1;

        this.inputItem = $(document.getElementById(id + "Input"));
        this.inputItemWidth = this.inputItem.width();
        this.inputWidthDefined = options.inputWidth !== undefined;
    };
    rf.ui.InplaceInput.extend(rf.ui.InplaceSelect);
    var $super = rf.ui.InplaceSelect.$super;

    var defaultOptions = {
        defaultLabel: "",
        saveOnSelect: true,
        openOnEdit: true,
        showControl: false,
        itemCss: "rf-is-opt",
        selectItemCss: "rf-is-sel",
        listCss: "rf-is-lst-cord",
        noneCss: "rf-is-none",
        editCss: "rf-is-fld-cntr",
        changedCss: "rf-is-chng"
    };

    $.extend(rf.ui.InplaceSelect.prototype, (function () {

        return{
            name : "inplaceSelect",
            defaultLabelClass : "rf-is-dflt-lbl",

            getName: function() {
                return this.name;
            },
            getNamespace: function() {
                return this.namespace;
            },
            onshow: function() {
                $super.onshow.call(this);
                if (this.openOnEdit) {
                    this.__showPopup();
                }
            },
            onhide: function() {
                $super.onhide.call(this); 
                this.__hidePopup();
            },

            showPopup: function() {
                $super.__show.call(this);
            },
            __showPopup: function() {
                this.popupList.show();
                this.__hideLabel();
                this.__setFocused(true);
            },
            hidePopup: function() {
                $super.__hide.call(this);
                this.__setFocused(false); 
            },
            __hidePopup: function() {
                this.popupList.hide();
                this.__showLabel();
            },

            onsave: function() {
                this.saveItemValue(this.__getValue());  
                this.list.__selectItemByValue(this.__getValue());  
                this.savedIndex = this.list.getSelectedItemIndex();  
            },
            oncancel: function() {
                var item = this.list.getClientSelectItemByIndex(this.savedIndex);
                var value = item && item.value ? item.value : this.initialValue;
                this.saveItemValue(value);
                this.list.__selectItemByValue(value);
            },
            onblur: function(e) {
                $super.onblur.call(this);
            },
            onfocus: function(e) {
                if (!this.__isFocused()) {
                    this.__setFocused(true);
                    this.focusValue = this.selValueInput.val();
                    this.invokeEvent.call(this, "focus", document.getElementById(this.id), e);
                }
            },
            processItem: function(item) {
                var label = $(item).data('clientSelectItem').label;
                this.__setValue(label);
                this.invokeEvent.call(this, "selectitem", document.getElementById(this.id));  
                if (this.saveOnSelect) {  
                    this.save();  
                    this.hidePopup();  
                }
            },
            saveItemValue: function(value) {
                this.selValueInput.val(value);

            },
            __isValueChanged: function() {
                return (this.focusValue != this.selValueInput.val());
            },
            __keydownHandler: function(e) {

                var code;

                if (e.keyCode) {
                    code = e.keyCode;
                } else if (e.which) {
                    code = e.which;
                }

                if (this.popupList.isVisible()) {
                    switch (code) {
                        case rf.KEYS.DOWN:
                            e.preventDefault();
                            this.list.__selectNext();
                            this.__setInputFocus();
                            break;

                        case rf.KEYS.UP:
                            e.preventDefault();
                            this.list.__selectPrev();
                            this.__setInputFocus();
                            break;

                        case rf.KEYS.RETURN:
                            e.preventDefault();
                            this.list.__selectCurrent();
                            this.__setInputFocus();
                            return false;
                            break;
                    }
                }

                $super.__keydownHandler.call(this, e);
            },
            __blurHandler: function(e) {
                if (!this.isMouseDown && this.isEditState()) {  
                    var _this = this;
                    this.timeoutId = window.setTimeout($.proxy(function() {  
                        _this.onblur(e);  
                    }, this), 200);  
                } 
            },
            __clickHandler: function(e) {
                this.__showPopup();
            },
            __onListMouseDown: function(e) {
                this.isMouseDown = true;
            },
            __onListMouseUp: function(e) {
                this.isMouseDown = false;
                this.__setInputFocus();
            },
            __showLabel: function(e) {
                this.label.show();
                this.editContainer.css("position", "absolute");
                this.inputItem.width(this.inputItemWidth);
            },
            __hideLabel: function(e) {
                this.label.hide();
                this.editContainer.css("position", "static");
                if (!this.inputWidthDefined) {
                    this.inputItem.width(this.label.width());
                }
            },
            getValue: function() {
                return this.selValueInput.val();
            },
            setValue: function(value) {
                var item = this.list.__selectItemByValue(value);
                var clientSelectItem = item.data('clientSelectItem');
                this.__setValue(clientSelectItem.label);
                if (this.__isValueChanged()) {
                    this.save();
                    this.invokeEvent.call(this, "change", document.getElementById(this.id));
                }
            },
            destroy: function() {
                this.popupList.destroy();
                this.popupList = null;
                $super.destroy.call(this);
            }
        };

    })());
})(jQuery, window.RichFaces);
