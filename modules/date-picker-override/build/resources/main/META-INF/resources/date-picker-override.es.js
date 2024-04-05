"use strict";

Liferay.Loader.define("dynamic-data-date-picker-override-form-field@1.0.0/date-picker-override.es", ['module', 'exports', 'require', 'liferay!frontend-js-react-web$react', 'dynamic-data-mapping-form-field-type/FieldBase/ReactFieldBase.es', 'dynamic-data-mapping-form-field-type/hooks/useSyncValue.es', '@frontend-taglib-clay$clayui/date-picker'], function (module, exports, require) {
  var define = undefined;
  var global = window;
  {
    function _typeof(o) {
      "@babel/helpers - typeof";
      return _typeof = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function (o) {
        return typeof o;
      } : function (o) {
        return o && "function" == typeof Symbol && o.constructor === Symbol && o !== Symbol.prototype ? "symbol" : typeof o;
      }, _typeof(o);
    }
    Object.defineProperty(exports, "__esModule", {
      value: true
    });
    exports["default"] = void 0;
    var _react = _interopRequireWildcard(require("liferay!frontend-js-react-web$react"));
    var _ReactFieldBase = require("dynamic-data-mapping-form-field-type/FieldBase/ReactFieldBase.es");
    var _useSyncValue = require("dynamic-data-mapping-form-field-type/hooks/useSyncValue.es");
    var _datePicker = _interopRequireDefault(require("@frontend-taglib-clay$clayui/date-picker"));
    var _excluded = ["label", "name", "onChange", "predefinedValue", "readOnly", "value", "min", "max"];
    function _interopRequireDefault(obj) {
      return obj && obj.__esModule ? obj : { "default": obj };
    }
    function _getRequireWildcardCache(e) {
      if ("function" != typeof WeakMap) return null;var r = new WeakMap(),
          t = new WeakMap();return (_getRequireWildcardCache = function _getRequireWildcardCache(e) {
        return e ? t : r;
      })(e);
    }
    function _interopRequireWildcard(e, r) {
      if (!r && e && e.__esModule) return e;if (null === e || "object" != _typeof(e) && "function" != typeof e) return { "default": e };var t = _getRequireWildcardCache(r);if (t && t.has(e)) return t.get(e);var n = { __proto__: null },
          a = Object.defineProperty && Object.getOwnPropertyDescriptor;for (var u in e) if ("default" !== u && {}.hasOwnProperty.call(e, u)) {
        var i = a ? Object.getOwnPropertyDescriptor(e, u) : null;i && (i.get || i.set) ? Object.defineProperty(n, u, i) : n[u] = e[u];
      }return n["default"] = e, t && t.set(e, n), n;
    }
    function _extends() {
      _extends = Object.assign ? Object.assign.bind() : function (target) {
        for (var i = 1; i < arguments.length; i++) {
          var source = arguments[i];for (var key in source) {
            if (Object.prototype.hasOwnProperty.call(source, key)) {
              target[key] = source[key];
            }
          }
        }return target;
      };return _extends.apply(this, arguments);
    }
    function _slicedToArray(arr, i) {
      return _arrayWithHoles(arr) || _iterableToArrayLimit(arr, i) || _unsupportedIterableToArray(arr, i) || _nonIterableRest();
    }
    function _nonIterableRest() {
      throw new TypeError("Invalid attempt to destructure non-iterable instance.\nIn order to be iterable, non-array objects must have a [Symbol.iterator]() method.");
    }
    function _unsupportedIterableToArray(o, minLen) {
      if (!o) return;if (typeof o === "string") return _arrayLikeToArray(o, minLen);var n = Object.prototype.toString.call(o).slice(8, -1);if (n === "Object" && o.constructor) n = o.constructor.name;if (n === "Map" || n === "Set") return Array.from(o);if (n === "Arguments" || /^(?:Ui|I)nt(?:8|16|32)(?:Clamped)?Array$/.test(n)) return _arrayLikeToArray(o, minLen);
    }
    function _arrayLikeToArray(arr, len) {
      if (len == null || len > arr.length) len = arr.length;for (var i = 0, arr2 = new Array(len); i < len; i++) arr2[i] = arr[i];return arr2;
    }
    function _iterableToArrayLimit(r, l) {
      var t = null == r ? null : "undefined" != typeof Symbol && r[Symbol.iterator] || r["@@iterator"];if (null != t) {
        var e,
            n,
            i,
            u,
            a = [],
            f = !0,
            o = !1;try {
          if (i = (t = t.call(r)).next, 0 === l) {
            if (Object(t) !== t) return;f = !1;
          } else for (; !(f = (e = i.call(t)).done) && (a.push(e.value), a.length !== l); f = !0);
        } catch (r) {
          o = !0, n = r;
        } finally {
          try {
            if (!f && null != t["return"] && (u = t["return"](), Object(u) !== u)) return;
          } finally {
            if (o) throw n;
          }
        }return a;
      }
    }
    function _arrayWithHoles(arr) {
      if (Array.isArray(arr)) return arr;
    }
    function _objectWithoutProperties(source, excluded) {
      if (source == null) return {};var target = _objectWithoutPropertiesLoose(source, excluded);var key, i;if (Object.getOwnPropertySymbols) {
        var sourceSymbolKeys = Object.getOwnPropertySymbols(source);for (i = 0; i < sourceSymbolKeys.length; i++) {
          key = sourceSymbolKeys[i];if (excluded.indexOf(key) >= 0) continue;if (!Object.prototype.propertyIsEnumerable.call(source, key)) continue;target[key] = source[key];
        }
      }return target;
    }
    function _objectWithoutPropertiesLoose(source, excluded) {
      if (source == null) return {};var target = {};var sourceKeys = Object.keys(source);var key, i;for (i = 0; i < sourceKeys.length; i++) {
        key = sourceKeys[i];if (excluded.indexOf(key) >= 0) continue;target[key] = source[key];
      }return target;
    }
    var Main = function Main(_ref) {
      var label = _ref.label,
          name = _ref.name,
          _onChange = _ref.onChange,
          predefinedValue = _ref.predefinedValue,
          readOnly = _ref.readOnly,
          value = _ref.value,
          min = _ref.min,
          max = _ref.max,
          otherProps = _objectWithoutProperties(_ref, _excluded);
      // const [currentValue, setCurrentValue] = useSyncValue(value ? value : predefinedValue);
      var initialValue = value !== null && value !== undefined ? value : predefinedValue;
      var _useState = (0, _react.useState)(initialValue),
          _useState2 = _slicedToArray(_useState, 2),
          currentValue = _useState2[0],
          setCurrentValue = _useState2[1];

      // Convert min and max to integers if they are provided as strings
      var defaultMin = min !== '' ? parseInt(min, 10) : 1997; // Default to 1997 if min is empty
      var defaultMax = max !== '' ? parseInt(max, 10) : 2024; // Default to 2024 if max is empty

      var currentDate = new Date().getDate(); // Get the current date (1-31)
      var currentMonth = new Date().getMonth() + 1;

      return (/*#__PURE__*/_react["default"].createElement(_ReactFieldBase.FieldBase, _extends({
          label: label,
          name: name,
          predefinedValue: predefinedValue
        }, otherProps), /*#__PURE__*/_react["default"].createElement(_datePicker["default"], {
          name: name,
          onChange: function onChange(date) {
            setCurrentValue(date);
            _onChange(date);
          },
          placeholder: "__/__/____",
          dateFormat: "MM/dd/yyyy"
          // dateFormat={clayFormat}

          , years: {
            end: defaultMax,
            start: defaultMin
          },
          value: currentValue,
          defaultMonth: defaultMax + "-" + currentMonth + "-" + currentDate
        }))
      );
    };
    Main.displayName = 'DatePickerOverride';
    var _default = exports["default"] = Main;
    //# sourceMappingURL=date-picker-override.es.js.map
  }
});
//# sourceMappingURL=date-picker-override.es.js.map