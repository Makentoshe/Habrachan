!function (t) {
    var e = {};

    function o(n) {
        if (e[n]) return e[n].exports;
        var r = e[n] = {i: n, l: !1, exports: {}};
        return t[n].call(r.exports, r, r.exports, o), r.l = !0, r.exports
    }

    o.m = t, o.c = e, o.d = function (t, e, n) {
        o.o(t, e) || Object.defineProperty(t, e, {enumerable: !0, get: n})
    }, o.r = function (t) {
        "undefined" != typeof Symbol && Symbol.toStringTag && Object.defineProperty(t, Symbol.toStringTag, {value: "Module"}), Object.defineProperty(t, "__esModule", {value: !0})
    }, o.t = function (t, e) {
        if (1 & e && (t = o(t)), 8 & e) return t;
        if (4 & e && "object" == typeof t && t && t.__esModule) return t;
        var n = Object.create(null);
        if (o.r(n), Object.defineProperty(n, "default", {
            enumerable: !0,
            value: t
        }), 2 & e && "string" != typeof t) for (var r in t) o.d(n, r, function (e) {
            return t[e]
        }.bind(null, r));
        return n
    }, o.n = function (t) {
        var e = t && t.__esModule ? function () {
            return t.default
        } : function () {
            return t
        };
        return o.d(e, "a", e), e
    }, o.o = function (t, e) {
        return Object.prototype.hasOwnProperty.call(t, e)
    }, o.p = "", o(o.s = 141)
}([, , function (t, e) {
    var o = function (t) {
        return void 0 !== t.redirect_debug ? ($.jGrowl('REDIRECT DEBUG: <a href="' + t.redirect_debug + '">' + t.redirect_debug + "</a>", {
            theme: "message",
            sticky: !0
        }), !0) : void 0 !== t.redirect && (document.location.href = t.redirect, !0)
    };

    function n(t) {
        if (void 0 !== t.system_warnings) for (var e in t.system_warnings) $.jGrowl(t.system_warnings[e], {theme: "warning"})
    }

    function r(t) {
        if (n(t), void 0 !== t.system_errors) for (var e in t.system_errors) $.jGrowl(t.system_errors[e], {theme: "error"})
    }

    function s(t) {
        if (void 0 !== t.errors) {
            for (var e in t.errors) classname = e.replace(/[\]\[]/g, ""), $("." + classname + " .error").html(t.errors[e]).fadeIn();
            $.scrollTo($(".error:visible").first(), 800, {axis: "y", offset: -150})
        }
        r(t)
    }

    t.exports = {
        ajaxFormBeforSubmit: function (t, e, o) {
            $(".error", e).text("").hide(), $('input[type="submit"], input[type="button"], button[type="button"], button[type="submit"]', e).attr("disabled", !0)
        }, ajaxFormError: function (t, e) {
            $('input[type="submit"], input[type="button"], button[type="button"], button[type="submit"]', e).attr("disabled", !1).removeClass("loading")
        }, ajaxFormRedirect: o, ajaxFormSuccess: function (t, e, n) {
            return function (n, r, a, i) {
                if ($('input[type="submit"], input[type="button"], button[type="button"], button[type="submit"]', this.form).attr("disabled", !1).removeClass("loading"), "ok" !== n.messages) return s(n);
                t(n, r, a, i), o(n), "function" == typeof e && e(n, r, a, i)
            }
        }, showFormErrors: s, showSystemError: r, showSystemWarnings: n
    }
}, , function (t, e, o) {
    "use strict";
    var n = o(14).default;
    o(21), (e = t.exports = n).default = e
}, , function (t, e, o) {
    "use strict";
    e.extend = function (t) {
        var e, o, r, s, a = Array.prototype.slice.call(arguments, 1);
        for (e = 0, o = a.length; e < o; e += 1) if (r = a[e]) for (s in r) n.call(r, s) && (t[s] = r[s]);
        return t
    };
    var n = Object.prototype.hasOwnProperty;
    e.hop = n
}, function (t, e, o) {
    window.hljs;

    function n(t, e) {
        t.toggleClass("hidden", !e)
    }

    void 0 !== t.exports && (t.exports = {
        enableCommentButtons: function (t, e) {
            var o = $(".js-form-buttons"), n = $('button[name="preview"]', o), r = $('button[name="send"]', o),
                s = $('button[name="save"]', o);
            n.prop("disabled", !e), r.prop("disabled", !e), s.prop("disabled", !e)
        }, findCommentById: function (t) {
            return $("#comment_" + t).parent(".js-comment")
        }, setCommentForm: function (t, e, o, n, r) {
            var s = $('input[name="parent_id"]', t), a = $('input[name="comment_id"]', t), i = $("#comment_text", t),
                c = $("#preview_placeholder", t);
            s.val(e), a.val(o), i.val(n), c.text(""), r || i.focus()
        }, showCommentButtons: function (t) {
            var e = $(".js-form-buttons");
            n($('button[type="button"]', e), !1);
            var o = $('button[name="preview"]', e), r = $('button[name="send"]', e), s = $('button[name="save"]', e),
                a = $('button[name="delete"]', e);
            switch (t) {
                case"reply":
                    n(o, !0), n(r, !0);
                    break;
                case"edit":
                    n(o, !0), n(s, !0), n(a, !0)
            }
        }, validateFormInput: function (t) {
            var e = t.find('textarea[name="text"]').val();
            return !!$.trim(e) || ($.jGrowl(window.i18nMsg("COMMENT_NOTICE_TEXT_REQIRED"), {theme: "error"}), !1)
        }, scrollToComment: function (t, e) {
            var o = $(t).parents(".js-comment_collapsed:last"), n = o.length > 0 ? o : t,
                r = parseInt($(n).css("padding-top"), 10),
                s = -(10 + parseInt($(".main-navbar").height(), 10)) + r || -10;
            o.removeClass("blink"), $.scrollTo(n, 100, {
                axis: "y", offset: s, onAfter: function () {
                    if (o.addClass("blink"), e instanceof Function) return e()
                }
            })
        }
    })
}, , , , , , function (t, e, o) {
    (function (t) {
        t.__addLocaleData({
            locale: "ru", pluralRuleFunction: function (t, e) {
                var o = String(t).split("."), n = o[0], r = !o[1], s = n.slice(-1), a = n.slice(-2);
                return e ? "other" : r && 1 == s && 11 != a ? "one" : r && s >= 2 && s <= 4 && (a < 12 || a > 14) ? "few" : r && 0 == s || r && s >= 5 && s <= 9 || r && a >= 11 && a <= 14 ? "many" : "other"
            }
        }), t.__addLocaleData({locale: "ru-BY", parentLocale: "ru"}), t.__addLocaleData({
            locale: "ru-KG",
            parentLocale: "ru"
        }), t.__addLocaleData({locale: "ru-KZ", parentLocale: "ru"}), t.__addLocaleData({
            locale: "ru-MD",
            parentLocale: "ru"
        }), t.__addLocaleData({locale: "ru-UA", parentLocale: "ru"})
    }).call(this, o(4))
}, function (t, e, o) {
    "use strict";
    var n = o(15), r = o(20);
    n.default.__addLocaleData(r.default), n.default.defaultLocale = "en", e.default = n.default
}, function (t, e, o) {
    "use strict";
    var n = o(6), r = o(16), s = o(17), a = o(18);

    function i(t, e, o) {
        var n = "string" == typeof t ? i.__parse(t) : t;
        if (!n || "messageFormatPattern" !== n.type) throw new TypeError("A message must be provided as a String or AST.");
        o = this._mergeFormats(i.formats, o), r.defineProperty(this, "_locale", {value: this._resolveLocale(e)});
        var s = this._findPluralRuleFunction(this._locale), a = this._compilePattern(n, e, o, s), c = this;
        this.format = function (e) {
            try {
                return c._format(a, e)
            } catch (e) {
                throw e.variableId ? new Error("The intl string context variable '" + e.variableId + "' was not provided to the string '" + t + "'") : e
            }
        }
    }

    e.default = i, r.defineProperty(i, "formats", {
        enumerable: !0,
        value: {
            number: {currency: {style: "currency"}, percent: {style: "percent"}},
            date: {
                short: {month: "numeric", day: "numeric", year: "2-digit"},
                medium: {month: "short", day: "numeric", year: "numeric"},
                long: {month: "long", day: "numeric", year: "numeric"},
                full: {weekday: "long", month: "long", day: "numeric", year: "numeric"}
            },
            time: {
                short: {hour: "numeric", minute: "numeric"},
                medium: {hour: "numeric", minute: "numeric", second: "numeric"},
                long: {hour: "numeric", minute: "numeric", second: "numeric", timeZoneName: "short"},
                full: {hour: "numeric", minute: "numeric", second: "numeric", timeZoneName: "short"}
            }
        }
    }), r.defineProperty(i, "__localeData__", {value: r.objCreate(null)}), r.defineProperty(i, "__addLocaleData", {
        value: function (t) {
            if (!t || !t.locale) throw new Error("Locale data provided to IntlMessageFormat is missing a `locale` property");
            i.__localeData__[t.locale.toLowerCase()] = t
        }
    }), r.defineProperty(i, "__parse", {value: a.default.parse}), r.defineProperty(i, "defaultLocale", {
        enumerable: !0,
        writable: !0,
        value: void 0
    }), i.prototype.resolvedOptions = function () {
        return {locale: this._locale}
    }, i.prototype._compilePattern = function (t, e, o, n) {
        return new s.default(e, o, n).compile(t)
    }, i.prototype._findPluralRuleFunction = function (t) {
        for (var e = i.__localeData__, o = e[t.toLowerCase()]; o;) {
            if (o.pluralRuleFunction) return o.pluralRuleFunction;
            o = o.parentLocale && e[o.parentLocale.toLowerCase()]
        }
        throw new Error("Locale data added to IntlMessageFormat is missing a `pluralRuleFunction` for :" + t)
    }, i.prototype._format = function (t, e) {
        var o, r, s, a, i, c, l = "";
        for (o = 0, r = t.length; o < r; o += 1) if ("string" != typeof (s = t[o])) {
            if (a = s.id, !e || !n.hop.call(e, a)) throw(c = new Error("A value must be provided for: " + a)).variableId = a, c;
            i = e[a], s.options ? l += this._format(s.getOption(i), e) : l += s.format(i)
        } else l += s;
        return l
    }, i.prototype._mergeFormats = function (t, e) {
        var o, s, a = {};
        for (o in t) n.hop.call(t, o) && (a[o] = s = r.objCreate(t[o]), e && n.hop.call(e, o) && n.extend(s, e[o]));
        return a
    }, i.prototype._resolveLocale = function (t) {
        "string" == typeof t && (t = [t]), t = (t || []).concat(i.defaultLocale);
        var e, o, n, r, s = i.__localeData__;
        for (e = 0, o = t.length; e < o; e += 1) for (n = t[e].toLowerCase().split("-"); n.length;) {
            if (r = s[n.join("-")]) return r.locale;
            n.pop()
        }
        var a = t.pop();
        throw new Error("No locale data has been added to IntlMessageFormat for: " + t.join(", ") + ", or the default locale: " + a)
    }
}, function (t, e, o) {
    "use strict";
    var n = o(6), r = function () {
        try {
            return !!Object.defineProperty({}, "a", {})
        } catch (t) {
            return !1
        }
    }(), s = (!r && Object.prototype.__defineGetter__, r ? Object.defineProperty : function (t, e, o) {
        "get" in o && t.__defineGetter__ ? t.__defineGetter__(e, o.get) : (!n.hop.call(t, e) || "value" in o) && (t[e] = o.value)
    }), a = Object.create || function (t, e) {
        var o, r;

        function a() {
        }

        for (r in a.prototype = t, o = new a, e) n.hop.call(e, r) && s(o, r, e[r]);
        return o
    };
    e.defineProperty = s, e.objCreate = a
}, function (t, e, o) {
    "use strict";

    function n(t, e, o) {
        this.locales = t, this.formats = e, this.pluralFn = o
    }

    function r(t) {
        this.id = t
    }

    function s(t, e, o, n, r) {
        this.id = t, this.useOrdinal = e, this.offset = o, this.options = n, this.pluralFn = r
    }

    function a(t, e, o, n) {
        this.id = t, this.offset = e, this.numberFormat = o, this.string = n
    }

    function i(t, e) {
        this.id = t, this.options = e
    }

    e.default = n, n.prototype.compile = function (t) {
        return this.pluralStack = [], this.currentPlural = null, this.pluralNumberFormat = null, this.compileMessage(t)
    }, n.prototype.compileMessage = function (t) {
        if (!t || "messageFormatPattern" !== t.type) throw new Error('Message AST is not of type: "messageFormatPattern"');
        var e, o, n, r = t.elements, s = [];
        for (e = 0, o = r.length; e < o; e += 1) switch ((n = r[e]).type) {
            case"messageTextElement":
                s.push(this.compileMessageText(n));
                break;
            case"argumentElement":
                s.push(this.compileArgument(n));
                break;
            default:
                throw new Error("Message element does not have a valid type")
        }
        return s
    }, n.prototype.compileMessageText = function (t) {
        return this.currentPlural && /(^|[^\\])#/g.test(t.value) ? (this.pluralNumberFormat || (this.pluralNumberFormat = new Intl.NumberFormat(this.locales)), new a(this.currentPlural.id, this.currentPlural.format.offset, this.pluralNumberFormat, t.value)) : t.value.replace(/\\#/g, "#")
    }, n.prototype.compileArgument = function (t) {
        var e = t.format;
        if (!e) return new r(t.id);
        var o, n = this.formats, a = this.locales, c = this.pluralFn;
        switch (e.type) {
            case"numberFormat":
                return o = n.number[e.style], {id: t.id, format: new Intl.NumberFormat(a, o).format};
            case"dateFormat":
                return o = n.date[e.style], {id: t.id, format: new Intl.DateTimeFormat(a, o).format};
            case"timeFormat":
                return o = n.time[e.style], {id: t.id, format: new Intl.DateTimeFormat(a, o).format};
            case"pluralFormat":
                return o = this.compileOptions(t), new s(t.id, e.ordinal, e.offset, o, c);
            case"selectFormat":
                return o = this.compileOptions(t), new i(t.id, o);
            default:
                throw new Error("Message element does not have a valid format type")
        }
    }, n.prototype.compileOptions = function (t) {
        var e, o, n, r = t.format, s = r.options, a = {};
        for (this.pluralStack.push(this.currentPlural), this.currentPlural = "pluralFormat" === r.type ? t : null, e = 0, o = s.length; e < o; e += 1) a[(n = s[e]).selector] = this.compileMessage(n.value);
        return this.currentPlural = this.pluralStack.pop(), a
    }, r.prototype.format = function (t) {
        return t || "number" == typeof t ? "string" == typeof t ? t : String(t) : ""
    }, s.prototype.getOption = function (t) {
        var e = this.options;
        return e["=" + t] || e[this.pluralFn(t - this.offset, this.useOrdinal)] || e.other
    }, a.prototype.format = function (t) {
        var e = this.numberFormat.format(t - this.offset);
        return this.string.replace(/(^|[^\\])#/g, "$1" + e).replace(/\\#/g, "#")
    }, i.prototype.getOption = function (t) {
        var e = this.options;
        return e[t] || e.other
    }
}, function (t, e, o) {
    "use strict";
    (e = t.exports = o(19).default).default = e
}, function (t, e, o) {
    "use strict";
    e.default = function () {
        function t(e, o, n, r) {
            this.message = e, this.expected = o, this.found = n, this.location = r, this.name = "SyntaxError", "function" == typeof Error.captureStackTrace && Error.captureStackTrace(this, t)
        }

        return function (t, e) {
            function o() {
                this.constructor = t
            }

            o.prototype = e.prototype, t.prototype = new o
        }(t, Error), {
            SyntaxError: t, parse: function (e) {
                var o, n = arguments.length > 1 ? arguments[1] : {}, r = {}, s = {start: yt}, a = yt, i = function (t) {
                        return {type: "messageFormatPattern", elements: t, location: Ot()}
                    }, c = function (t) {
                        var e, o, n, r, s, a = "";
                        for (e = 0, n = t.length; e < n; e += 1) for (o = 0, s = (r = t[e]).length; o < s; o += 1) a += r[o];
                        return a
                    }, l = function (t) {
                        return {type: "messageTextElement", value: t, location: Ot()}
                    }, u = /^[^ \t\n\r,.+={}#]/,
                    d = {type: "class", value: "[^ \\t\\n\\r,.+={}#]", description: "[^ \\t\\n\\r,.+={}#]"}, _ = "{",
                    m = {type: "literal", value: "{", description: '"{"'}, f = ",",
                    p = {type: "literal", value: ",", description: '","'}, h = "}",
                    g = {type: "literal", value: "}", description: '"}"'}, E = function (t, e) {
                        return {type: "argumentElement", id: t, format: e && e[2], location: Ot()}
                    }, T = "number", v = {type: "literal", value: "number", description: '"number"'}, b = "date",
                    S = {type: "literal", value: "date", description: '"date"'}, $ = "time",
                    w = {type: "literal", value: "time", description: '"time"'}, O = function (t, e) {
                        return {type: t + "Format", style: e && e[2], location: Ot()}
                    }, C = "plural", A = {type: "literal", value: "plural", description: '"plural"'}, N = function (t) {
                        return {type: t.type, ordinal: !1, offset: t.offset || 0, options: t.options, location: Ot()}
                    }, I = "selectordinal", y = {type: "literal", value: "selectordinal", description: '"selectordinal"'},
                    R = function (t) {
                        return {type: t.type, ordinal: !0, offset: t.offset || 0, options: t.options, location: Ot()}
                    }, M = "select", L = {type: "literal", value: "select", description: '"select"'}, P = function (t) {
                        return {type: "selectFormat", options: t, location: Ot()}
                    }, j = "=", k = {type: "literal", value: "=", description: '"="'}, D = function (t, e) {
                        return {type: "optionalFormatPattern", selector: t, value: e, location: Ot()}
                    }, x = "offset:", F = {type: "literal", value: "offset:", description: '"offset:"'}, U = function (t) {
                        return t
                    }, G = function (t, e) {
                        return {type: "pluralFormat", offset: t, options: e, location: Ot()}
                    }, B = {type: "other", description: "whitespace"}, W = /^[ \t\n\r]/,
                    Y = {type: "class", value: "[ \\t\\n\\r]", description: "[ \\t\\n\\r]"},
                    V = {type: "other", description: "optionalWhitespace"}, H = /^[0-9]/,
                    X = {type: "class", value: "[0-9]", description: "[0-9]"}, K = /^[0-9a-f]/i,
                    q = {type: "class", value: "[0-9a-f]i", description: "[0-9a-f]i"}, z = "0",
                    Q = {type: "literal", value: "0", description: '"0"'}, J = /^[1-9]/,
                    Z = {type: "class", value: "[1-9]", description: "[1-9]"}, tt = function (t) {
                        return parseInt(t, 10)
                    }, et = /^[^{}\\\0-\x1F \t\n\r]/, ot = {
                        type: "class",
                        value: "[^{}\\\\\\0-\\x1F\\x7f \\t\\n\\r]",
                        description: "[^{}\\\\\\0-\\x1F\\x7f \\t\\n\\r]"
                    }, nt = "\\\\", rt = {type: "literal", value: "\\\\", description: '"\\\\\\\\"'}, st = function () {
                        return "\\"
                    }, at = "\\#", it = {type: "literal", value: "\\#", description: '"\\\\#"'}, ct = function () {
                        return "\\#"
                    }, lt = "\\{", ut = {type: "literal", value: "\\{", description: '"\\\\{"'}, dt = function () {
                        return "{"
                    }, _t = "\\}", mt = {type: "literal", value: "\\}", description: '"\\\\}"'}, ft = function () {
                        return "}"
                    }, pt = "\\u", ht = {type: "literal", value: "\\u", description: '"\\\\u"'}, gt = function (t) {
                        return String.fromCharCode(parseInt(t, 16))
                    }, Et = function (t) {
                        return t.join("")
                    }, Tt = 0, vt = 0, bt = [{line: 1, column: 1, seenCR: !1}], St = 0, $t = [], wt = 0;
                if ("startRule" in n) {
                    if (!(n.startRule in s)) throw new Error("Can't start parsing from rule \"" + n.startRule + '".');
                    a = s[n.startRule]
                }

                function Ot() {
                    return At(vt, Tt)
                }

                function Ct(t) {
                    var o, n, r = bt[t];
                    if (r) return r;
                    for (o = t - 1; !bt[o];) o--;
                    for (r = {
                        line: (r = bt[o]).line,
                        column: r.column,
                        seenCR: r.seenCR
                    }; o < t;) "\n" === (n = e.charAt(o)) ? (r.seenCR || r.line++, r.column = 1, r.seenCR = !1) : "\r" === n || "\u2028" === n || "\u2029" === n ? (r.line++, r.column = 1, r.seenCR = !0) : (r.column++, r.seenCR = !1), o++;
                    return bt[t] = r, r
                }

                function At(t, e) {
                    var o = Ct(t), n = Ct(e);
                    return {
                        start: {offset: t, line: o.line, column: o.column},
                        end: {offset: e, line: n.line, column: n.column}
                    }
                }

                function Nt(t) {
                    Tt < St || (Tt > St && (St = Tt, $t = []), $t.push(t))
                }

                function It(e, o, n, r) {
                    return null !== o && function (t) {
                        var e = 1;
                        for (t.sort(function (t, e) {
                            return t.description < e.description ? -1 : t.description > e.description ? 1 : 0
                        }); e < t.length;) t[e - 1] === t[e] ? t.splice(e, 1) : e++
                    }(o), new t(null !== e ? e : function (t, e) {
                        var o, n = new Array(t.length);
                        for (o = 0; o < t.length; o++) n[o] = t[o].description;
                        return "Expected " + (t.length > 1 ? n.slice(0, -1).join(", ") + " or " + n[t.length - 1] : n[0]) + " but " + (e ? '"' + function (t) {
                            function e(t) {
                                return t.charCodeAt(0).toString(16).toUpperCase()
                            }

                            return t.replace(/\\/g, "\\\\").replace(/"/g, '\\"').replace(/\x08/g, "\\b").replace(/\t/g, "\\t").replace(/\n/g, "\\n").replace(/\f/g, "\\f").replace(/\r/g, "\\r").replace(/[\x00-\x07\x0B\x0E\x0F]/g, function (t) {
                                return "\\x0" + e(t)
                            }).replace(/[\x10-\x1F\x80-\xFF]/g, function (t) {
                                return "\\x" + e(t)
                            }).replace(/[\u0100-\u0FFF]/g, function (t) {
                                return "\\u0" + e(t)
                            }).replace(/[\u1000-\uFFFF]/g, function (t) {
                                return "\\u" + e(t)
                            })
                        }(e) + '"' : "end of input") + " found."
                    }(o, n), o, n, r)
                }

                function yt() {
                    return Rt()
                }

                function Rt() {
                    var t, e, o;
                    for (t = Tt, e = [], o = Mt(); o !== r;) e.push(o), o = Mt();
                    return e !== r && (vt = t, e = i(e)), t = e
                }

                function Mt() {
                    var t;
                    return (t = function () {
                        var t, o;
                        return t = Tt, (o = function () {
                            var t, o, n, s, a, i;
                            if (t = Tt, o = [], n = Tt, (s = kt()) !== r && (a = Gt()) !== r && (i = kt()) !== r ? n = s = [s, a, i] : (Tt = n, n = r), n !== r) for (; n !== r;) o.push(n), n = Tt, (s = kt()) !== r && (a = Gt()) !== r && (i = kt()) !== r ? n = s = [s, a, i] : (Tt = n, n = r); else o = r;
                            return o !== r && (vt = t, o = c(o)), (t = o) === r && (t = Tt, o = jt(), t = o !== r ? e.substring(t, Tt) : o), t
                        }()) !== r && (vt = t, o = l(o)), t = o
                    }()) === r && (t = function () {
                        var t, o, n, s, a, i, c;
                        return t = Tt, 123 === e.charCodeAt(Tt) ? (o = _, Tt++) : (o = r, 0 === wt && Nt(m)), o !== r && kt() !== r && (n = function () {
                            var t, o, n;
                            if ((t = Ft()) === r) {
                                if (t = Tt, o = [], u.test(e.charAt(Tt)) ? (n = e.charAt(Tt), Tt++) : (n = r, 0 === wt && Nt(d)), n !== r) for (; n !== r;) o.push(n), u.test(e.charAt(Tt)) ? (n = e.charAt(Tt), Tt++) : (n = r, 0 === wt && Nt(d)); else o = r;
                                t = o !== r ? e.substring(t, Tt) : o
                            }
                            return t
                        }()) !== r && kt() !== r ? (s = Tt, 44 === e.charCodeAt(Tt) ? (a = f, Tt++) : (a = r, 0 === wt && Nt(p)), a !== r && (i = kt()) !== r && (c = function () {
                            var t;
                            return (t = function () {
                                var t, o, n, s, a, i;
                                return t = Tt, e.substr(Tt, 6) === T ? (o = T, Tt += 6) : (o = r, 0 === wt && Nt(v)), o === r && (e.substr(Tt, 4) === b ? (o = b, Tt += 4) : (o = r, 0 === wt && Nt(S)), o === r && (e.substr(Tt, 4) === $ ? (o = $, Tt += 4) : (o = r, 0 === wt && Nt(w)))), o !== r && kt() !== r ? (n = Tt, 44 === e.charCodeAt(Tt) ? (s = f, Tt++) : (s = r, 0 === wt && Nt(p)), s !== r && (a = kt()) !== r && (i = Gt()) !== r ? n = s = [s, a, i] : (Tt = n, n = r), n === r && (n = null), n !== r ? (vt = t, o = O(o, n), t = o) : (Tt = t, t = r)) : (Tt = t, t = r), t
                            }()) === r && (t = function () {
                                var t, o, n, s;
                                return t = Tt, e.substr(Tt, 6) === C ? (o = C, Tt += 6) : (o = r, 0 === wt && Nt(A)), o !== r && kt() !== r ? (44 === e.charCodeAt(Tt) ? (n = f, Tt++) : (n = r, 0 === wt && Nt(p)), n !== r && kt() !== r && (s = Pt()) !== r ? (vt = t, o = N(s), t = o) : (Tt = t, t = r)) : (Tt = t, t = r), t
                            }()) === r && (t = function () {
                                var t, o, n, s;
                                return t = Tt, e.substr(Tt, 13) === I ? (o = I, Tt += 13) : (o = r, 0 === wt && Nt(y)), o !== r && kt() !== r ? (44 === e.charCodeAt(Tt) ? (n = f, Tt++) : (n = r, 0 === wt && Nt(p)), n !== r && kt() !== r && (s = Pt()) !== r ? (vt = t, o = R(s), t = o) : (Tt = t, t = r)) : (Tt = t, t = r), t
                            }()) === r && (t = function () {
                                var t, o, n, s, a;
                                if (t = Tt, e.substr(Tt, 6) === M ? (o = M, Tt += 6) : (o = r, 0 === wt && Nt(L)), o !== r) if (kt() !== r) if (44 === e.charCodeAt(Tt) ? (n = f, Tt++) : (n = r, 0 === wt && Nt(p)), n !== r) if (kt() !== r) {
                                    if (s = [], (a = Lt()) !== r) for (; a !== r;) s.push(a), a = Lt(); else s = r;
                                    s !== r ? (vt = t, o = P(s), t = o) : (Tt = t, t = r)
                                } else Tt = t, t = r; else Tt = t, t = r; else Tt = t, t = r; else Tt = t, t = r;
                                return t
                            }()), t
                        }()) !== r ? s = a = [a, i, c] : (Tt = s, s = r), s === r && (s = null), s !== r && (a = kt()) !== r ? (125 === e.charCodeAt(Tt) ? (i = h, Tt++) : (i = r, 0 === wt && Nt(g)), i !== r ? (vt = t, o = E(n, s), t = o) : (Tt = t, t = r)) : (Tt = t, t = r)) : (Tt = t, t = r), t
                    }()), t
                }

                function Lt() {
                    var t, o, n, s, a;
                    return t = Tt, kt() !== r && (o = function () {
                        var t, o, n, s;
                        return t = Tt, o = Tt, 61 === e.charCodeAt(Tt) ? (n = j, Tt++) : (n = r, 0 === wt && Nt(k)), n !== r && (s = Ft()) !== r ? o = n = [n, s] : (Tt = o, o = r), (t = o !== r ? e.substring(t, Tt) : o) === r && (t = Gt()), t
                    }()) !== r && kt() !== r ? (123 === e.charCodeAt(Tt) ? (n = _, Tt++) : (n = r, 0 === wt && Nt(m)), n !== r && kt() !== r && (s = Rt()) !== r && kt() !== r ? (125 === e.charCodeAt(Tt) ? (a = h, Tt++) : (a = r, 0 === wt && Nt(g)), a !== r ? (vt = t, t = D(o, s)) : (Tt = t, t = r)) : (Tt = t, t = r)) : (Tt = t, t = r), t
                }

                function Pt() {
                    var t, o, n, s;
                    if (t = Tt, (o = function () {
                        var t, o, n;
                        return t = Tt, e.substr(Tt, 7) === x ? (o = x, Tt += 7) : (o = r, 0 === wt && Nt(F)), o !== r && kt() !== r && (n = Ft()) !== r ? (vt = t, t = o = U(n)) : (Tt = t, t = r), t
                    }()) === r && (o = null), o !== r) if (kt() !== r) {
                        if (n = [], (s = Lt()) !== r) for (; s !== r;) n.push(s), s = Lt(); else n = r;
                        n !== r ? (vt = t, t = o = G(o, n)) : (Tt = t, t = r)
                    } else Tt = t, t = r; else Tt = t, t = r;
                    return t
                }

                function jt() {
                    var t, o;
                    if (wt++, t = [], W.test(e.charAt(Tt)) ? (o = e.charAt(Tt), Tt++) : (o = r, 0 === wt && Nt(Y)), o !== r) for (; o !== r;) t.push(o), W.test(e.charAt(Tt)) ? (o = e.charAt(Tt), Tt++) : (o = r, 0 === wt && Nt(Y)); else t = r;
                    return wt--, t === r && (o = r, 0 === wt && Nt(B)), t
                }

                function kt() {
                    var t, o, n;
                    for (wt++, t = Tt, o = [], n = jt(); n !== r;) o.push(n), n = jt();
                    return t = o !== r ? e.substring(t, Tt) : o, wt--, t === r && (o = r, 0 === wt && Nt(V)), t
                }

                function Dt() {
                    var t;
                    return H.test(e.charAt(Tt)) ? (t = e.charAt(Tt), Tt++) : (t = r, 0 === wt && Nt(X)), t
                }

                function xt() {
                    var t;
                    return K.test(e.charAt(Tt)) ? (t = e.charAt(Tt), Tt++) : (t = r, 0 === wt && Nt(q)), t
                }

                function Ft() {
                    var t, o, n, s, a, i;
                    if (t = Tt, 48 === e.charCodeAt(Tt) ? (o = z, Tt++) : (o = r, 0 === wt && Nt(Q)), o === r) {
                        if (o = Tt, n = Tt, J.test(e.charAt(Tt)) ? (s = e.charAt(Tt), Tt++) : (s = r, 0 === wt && Nt(Z)), s !== r) {
                            for (a = [], i = Dt(); i !== r;) a.push(i), i = Dt();
                            a !== r ? n = s = [s, a] : (Tt = n, n = r)
                        } else Tt = n, n = r;
                        o = n !== r ? e.substring(o, Tt) : n
                    }
                    return o !== r && (vt = t, o = tt(o)), t = o
                }

                function Ut() {
                    var t, o, n, s, a, i, c, l;
                    return et.test(e.charAt(Tt)) ? (t = e.charAt(Tt), Tt++) : (t = r, 0 === wt && Nt(ot)), t === r && (t = Tt, e.substr(Tt, 2) === nt ? (o = nt, Tt += 2) : (o = r, 0 === wt && Nt(rt)), o !== r && (vt = t, o = st()), (t = o) === r && (t = Tt, e.substr(Tt, 2) === at ? (o = at, Tt += 2) : (o = r, 0 === wt && Nt(it)), o !== r && (vt = t, o = ct()), (t = o) === r && (t = Tt, e.substr(Tt, 2) === lt ? (o = lt, Tt += 2) : (o = r, 0 === wt && Nt(ut)), o !== r && (vt = t, o = dt()), (t = o) === r && (t = Tt, e.substr(Tt, 2) === _t ? (o = _t, Tt += 2) : (o = r, 0 === wt && Nt(mt)), o !== r && (vt = t, o = ft()), (t = o) === r && (t = Tt, e.substr(Tt, 2) === pt ? (o = pt, Tt += 2) : (o = r, 0 === wt && Nt(ht)), o !== r ? (n = Tt, s = Tt, (a = xt()) !== r && (i = xt()) !== r && (c = xt()) !== r && (l = xt()) !== r ? s = a = [a, i, c, l] : (Tt = s, s = r), (n = s !== r ? e.substring(n, Tt) : s) !== r ? (vt = t, t = o = gt(n)) : (Tt = t, t = r)) : (Tt = t, t = r)))))), t
                }

                function Gt() {
                    var t, e, o;
                    if (t = Tt, e = [], (o = Ut()) !== r) for (; o !== r;) e.push(o), o = Ut(); else e = r;
                    return e !== r && (vt = t, e = Et(e)), t = e
                }

                if ((o = a()) !== r && Tt === e.length) return o;
                throw o !== r && Tt < e.length && Nt({
                    type: "end",
                    description: "end of input"
                }), It(null, $t, St < e.length ? e.charAt(St) : null, St < e.length ? At(St, St + 1) : At(St, St))
            }
        }
    }()
}, function (t, e, o) {
    "use strict";
    e.default = {
        locale: "en", pluralRuleFunction: function (t, e) {
            var o = String(t).split("."), n = !o[1], r = Number(o[0]) == t, s = r && o[0].slice(-1),
                a = r && o[0].slice(-2);
            return e ? 1 == s && 11 != a ? "one" : 2 == s && 12 != a ? "two" : 3 == s && 13 != a ? "few" : "other" : 1 == t && n ? "one" : "other"
        }
    }
}, function (t, e) {
}, function (t, e, o) {
    var n = o(25), r = o(26);
    t.exports = {ru: n, en: r}
}, function (t, e) {
    t.exports = {
        instagrammProcess: function () {
            "undefined" != typeof instgrm && instgrm.Embeds.process()
        }
    }
}, function (t, e, o) {
    o(13);
    var n = o(4), r = o(22);
    n.__addLocaleData({locale: "ru_RU", parentLocale: "ru"}), n.__addLocaleData({
        locale: "en_US",
        parentLocale: "en"
    }), t.exports = {
        msg: function (t, e) {
            var o = window.g_current_hl;
            if (o) {
                var s = r[o], a = e || {};
                if (!s) return "MISSED TEXT";
                try {
                    return new n(s[t], o).format(a)
                } catch (t) {
                    return "MISSED TEXT"
                }
            }
        }
    }
}, function (t) {
    t.exports = JSON.parse('{"ABUSE_SUBMITTED":"Спасибо, жалоба отправлена","ACCOUNT_FORM_SETTING_SUCESS_MESSAGE":"Новые настройки успешно сохранились","APP_WAS_SUBMITTED":"Ваша заявка отправлена! С вами скоро свяжутся.","ATTENTION_MESSAGE":"Минуточку внимания","BETA_COOKIE_ENABLE_ALERT":"Включите cookie для возможности тестирования","BETA_COOKIE_ENABLE":"Включить обновление","BETA_COOKIE_ENABLED":"Обновление включено","CAPTCHA_IMAGE_MESSAGE":"Вы должны ввести 6 символов с картинки","CLOSE_BUTTON_TEXT":"Закрыть","CLOSE_ALL_NOTICES":"Закрыть все уведомления","COMMENT_ABUSE_CONFIRM":"Вы уверены, что хотите пожаловаться на комментарий?","COMMENT_ABUSE_NOTICE":"Cпасибо, жалоба отправлена","COMMENT_FAVORITE_ADDED":"Комментарий добавлен в закладки","COMMENT_FAVORITE_REMOVED":"Комментарий удален из закладок","COMMENT_HIDDEN":"Комментарий скрыт","COMMENT_NOTICE_TEXT_REQIRED":"Нельзя отправить пустой комментарий","COMMENT_REMOVE_CONFIRM":"Точно удаляем?","COMMENT_WAS_EDITED_TITLE":"Kомментарий был изменён","COMMENTS_COUNTDOWN_TIMER_LEFT":"Время истекло","COMMENTS_REMOVED_HIDE":"Скрыть удаленные комментарии","COMMENTS_REMOVED_SHOW":"Показать удаленные комментарии","COMMENTS_SETTINGS_SAVED_NOTICE":"Настройки уведомления о новых комментариях успешно сохранены","COMPANY_DONT_MEMBER":"Вы не работаете в этой компании","COMPANY_MEMBER_I":"Я работаю в этой компании","COMPANY_MEMBER_TOU":"Вы работаете в этой компании","COMPANY_QUIT_ACTION":"Уволиться из компании","CONVERSATION_REMOVE_NOTICE":"Вы уверены, что хотите удалить диалог?","CONVERSATION_INVITE_USER":"Вы уверены, что хотите пригласить этого пользователя?","DRAFT_REMOVE_ACTION_ALERT":"Вы уверены, что хотите удалить черновик?","FAVORITES_ADD_TITLE_ATTR":"Добавить в закладки","FAVORITES_REMOVE_TITLE_ATTR":"Удалить из закладок","FEEDBACK_FORM_SUCESS_MESSAGE":"Ваше сообщение отправлено. Сохраняйте терпение, в ближайшее время вы получите ответ.","FIELD_VALIDATION_ERROR_FIELD_TYPE":"Поле типа","FIELD_VALIDATION_ERROR_LENGTH":"Длина сообщения","FIELD_VALIDATION_ERROR_MAXLENGTH":"Доступное количество символов для ввода","FIELD_VALIDATION_ERROR_MINLENGTH":"Минимальное количество символов","FIELD_VALIDATION_ERROR_REQUIRED_KEYWORDS":"Необходимо заполнить поле, указав ключевые слова, характеризующие вашу публикацию","FIELD_VALIDATION_ERROR_REQUIRED":"Необходимо заполнить поле","FORM_NOTICE_IMAGE_REMOVED":"Изображение удалено","FORM_NOTICE_UPLOAD_FAILED":"Загрузка не удалась","FORM_NOTICE_UPLOAD_SUCESSFULL":"Файл успешно загружен","FORM_SELECT_CITY_PLACEHOLDER":"Город","FORM_SELECT_REGION_PLACEHOLDER":"Регион","FORM_UPLOAD_BUTTON_TEXT":"Выберите файл для загрузки","FORM_UPLOAD_REMOVE_BUTTON_TEXT":"удалить или изменить","FORM_VALIDATION_ERROR_MESSAGE":"Проверьте правильно ли заполнены поля формы","FORM_SETTINGS_WAS_SAVED_SUCCESSFULL":"Данные успешно сохранены","GENERATED_ROWS_REMOVE_LINK":"Удалить ссылку","HABRACUT_TEXTLENGTH_WARNING":"Текст публикации содержит {length} символов. Вам необходимо вставить тег &lt;cut/&gt;, если текст публикации содержит больше 1000 символов.","HUB_REQUEST_NOT_FOUND":"Не удалось найти хаб по запросу","INSTALLED_APPS_EMPTY_MESSAGE":"У вас нет установленных приложений","INVITE_GIVE_COUNTER":"Вы подарили приглашение.<br> У вас осталось {invites_string}.","INVITE_WAS_ACTIVED":"Приглашение активировано","INVITE_WAS_REVOKED":"Приглашение отозвано","INVITE_REVOKE_NOTICE":"Вы уверены, что хотите отозвать приглашение?","INVITE_WAS_SENT_AGAIN":"Приглашение повторно отправлено","INVITE_WAS_SENT":"Приглашение отправлено","KARMA_VOTES_COUNT_ALL":"Всего {votes_count}","COPIED":"Скопировано в буфер обмена","COPY_TEX_CODE":"Копировать TeX код","LANGUAGE_FORM_SUCCESS_MESSAGE":"Настройки успешно сохранены","MODERATION_POST_CONFIRM_MESSAGE":"Вы уверены, что хотите отправить текст на модерацию?","NOTICE_SAVED":"Заметка успешно сохранена","NOTICE_DELETED":"Заметка успешно удалена","PASSWORD_HAS_BEEN_CHANGED":"Пароль изменен","PASSWORD_RESTORE_LINK_MAIL":"Ссылка для восстановления пароля отправлена на указанный при регистрации адрес электронной почты.","PASSWORD_WAS_CHANGED_SUCCESSFULL":"Пароль успешно изменен. Теперь вы можете <a href=\'/login/\'>войти на сайт</a> с новым паролем.","POST_ACTION_FAVORITES_ADDED":"Публикация добавлена в закладки","POST_ACTION_FAVORITES_REMOVED":"Публикация удалена из закладок","POST_PUBLISH_ACTION_ALERT":"Вы уверены, что хотите опубликовать статью?","POST_FORM_HAS_ERRORS":"Форма заполнена неверно, проверьте, пожалуйста, все поля формы и попробуйте еще раз","POST_FORM_UNKONWN_ERROR":"Незивестная ошибка, обратитесь, пожалуйста, в службу поддержки","POSTS_TYPO_FORM_SUCCESS_SEND":"Сообщение об ошибке отправлено автору","POST_VIEWS_COUNT_LABEL":"Просмотры публикации","PPA_WITHDRAWAL_FUNDS_DISABLED":"Автоматический вывод средств отключен","PPA_WITHDRAWAL_FUNDS_ENABLED":"Автоматический вывод средств включен","QUESTION_REMOVE_CONFIRM":"Вы действительно хотите удалить вопрос?","QUIZ_POLL_FAILED_NOTICE":"К сожалению, вы ошиблись в одном из ответов. Попробуйте повторить попытку","QUIZ_POLL_SUCESS_NOTICE":"Поздравляем, теперь вы с нами. Добро пожаловать на","REJECT_REASON_DOESNT_CHOISE":"Вы не выбрали причину отклонения публикации!","REMOVE_ACTION_BUTTON_TEXT":"удалить","REQUEST_WAS_SUBMITTED_MODERATION":"Ваша заявка отправлена на модерацию","REVOKE_INVITE":"Отозвать приглашение","SAVE_SETTINGS":"Сохранить настройки","SCROLL_DOWN":"Вниз","SCROLL_TOP":"Наверх","SELECT_DEFAUL_OPTION_HUB":"Хаб","SEMINAR_PAGE_TITLE":"Хабр / Семинары","SETTINGS_LANGUAGE_INTERFACE":"Интерфейс","SETTINGS_LANGUAGE_PUBLICATIONS":"Язык публикаций","SETTINGS_LANGUAGE":"Настройка языка","SETTINGS_LANGUAGE_FEED_RU":"Русский","SETTINGS_LANGUAGE_FEED_EN":"Английский","SETTINGS_LANGUAGE_ERROR_MESSAGE":"Нужно выбрать хотя бы 1 язык","SHOW_MORE_BUTTON_TEXT":"дальше","SOCIAL_PAGE_CONNECT_ACTION":"Подключить","SUBSCRIBE_BUTTON_TEXT":"Подписаться","SUBSCRIBED_BUTTON_TEXT":"Подписан","FOLLOW_BUTTON_TEXT":"Подписаться","UNFOLLOW_BUTTON_TEXT":"Отписаться","FOLLOWING_BUTTON_TEXT":"Подписан","TOGGLE_BRANCH":"Раскрыть ветку","TOPIC_FORM_AUTOSAVE_INFO":"У вас есть автосохранение от {date}, <a href=\'#reset\' class=\'restore_form_data dotted\'>Восстановить форму</a>?","TOPIC_FORM_IMAGE_SRC_PROMPT":"Введите src картинки","TOPIC_FORM_LINK_URL_PROMPT":"Введите URL ссылки","TOPIC_FORM_PREVIEW_MODE_MESSAGE":"Вы находитесь в режиме предпросмотра","TOPIC_FORM_SINGLETON_NOTICE":"Вместе с хабом &laquo;Я пиарюсь&raquo; нельзя выбирать другие хабы.","TOPIC_FORM_SPOILER_TITLE":"Заголовок спойлера","TOPIC_FORM_USER_NICKNAME":"Введите никнейм пользователя","UNSUBSCRIBE_BUTTON_TEXT":"Отписаться","UPLOAD_FORM_PROGRESS":"Загружено {received} из {size}","USER_DEFAULT_DESCRIPTION":"Пользователь","USER_SENT_PIS":"Пользователь отправлен на ПИС","USER_SENT_PPG":"Пользователь отправлен на ППГ","VOTE_ACTION_MINUS":"Вы проголосовали отрицательно","VOTE_ACTION_PLUS":"Вы проголосовали положительно","VOTES_COUNTS_TITLE_ATTR":"Всего голосов {votes_count}: &uarr; {votes_count_plus} и &darr; {votes_count_minus}","XPANEL_COUNTER_TITLE_ATTR":"Количество новых комментариев. По клику (горячая клавиша - f) переходит к первому непрочитанному комментарию.","XPANEL_NEXT_BUTTON_TITLE_ATTR":"По клику (горячая клавиша - j) переходит к следующему непрочитанному комментарию.","XPANEL_PREV_BUTTON_TITLE_ATTR":"По клику (горячая клавиша - k) переходит к предыдущему непрочитанному комментарию.","COMPANY_ADMIN_PUBLICATION_SHOWN":"Публикация <strong>не будет видна</strong> в блоге компании","COMPANY_ADMIN_PUBLICATION_HIDDEN":"Публикация <strong>не будет видна</strong> в блоге компании","COMPANY_ADMIN_PLAN_NOTICE":"Тарифный план карточки компании будет изменён после окончания срока действия текущего тарифного плана","COMPANY_ADMIN_BRANDING_CLEAR_NOTIFICATION":"Вы уверены, что хотите удалить оформление?","COMPANY_ADMIN_FAVICON_WAS_UPDATE":"Favicon обновлена","COMPANY_ADMIN_REQUEST_WAS_APPROVED":"Заявка принята!<br/> На&nbsp;вашу почту отправлена счёт-оферта (иногда эти письма попадают в&nbsp;спам). Также платёжные документы находятся в&nbsp;разделе Финансы. <br/> В&nbsp;течение суток после оплаты счёта мы&nbsp;отправим письмо с&nbsp;кодом активации блога на&nbsp;вашу почту. Этот код нужно будет вставить в&nbsp;разделе Код активации.<br/>По&nbsp;всем вопросам пишите на&nbsp;<a href=\'mailto:corp@habr.team\'>corp@habr.team</a>","COMPANY_ADMIN_STARTUP_REQUEST_WAS_APPROVED":"Заявка принята! После модерации мы отправим письмо с кодом активации блога на вашу почту. Этот код нужно будет вставить в разделе Код активации.","FORM_INFO_WAS_SAVED":"Информация сохранена","COMPANY_ADMIN_VACANCY_HIDDEN":"Вакансия <b>не будет</b> отображаться в карточке компании","COMPANY_ADMIN_VACANCY_SHOWN":"Вакансия <b>будет</b> отображаться в карточке компании","COMPANY_ADMIN_WIDGET_WAS_PUBLISHED":"Виджет опубликован на сайте","COMPANY_ADMIN_WIDGET_WAS_HIDE":"Виджет скрыт","COMPANY_ADMIN_APP_WIDGET_WAS_PUBLISHED":"Виджет приложения опубликован на сайте","COMPANY_ADMIN_APP_WIDGET_WAS_HIDE":"Виджет приложения скрыт","COMPANY_ADMIN_WIDGET_SETTINGS_WERE_SAVED":"Настройки виджета успешно сохранились","COMPANY_ADMIN_EMPLOYEE_ROLE_DISABLED":"сотрудник","COMPANY_ADMIN_EMPLOYEE_ROLE_AUTHOR":"автор","COMPANY_ADMIN_EMPLOYEE_ROLE_WORKER":"редактор","COMPANY_ADMIN_EMPLOYEE_ROLE_ADMIN":"администратор","COMPANY_ADMIN_EMPLOYEE_REMOVE_CONFIRM":"Вы действительно хотите удалить сотрудника?","COMPANY_ADMIN_EMPLOYEE_NOTIFICATIONS_ON":"Пользователь {worker_login} <b>будет получать системные уведомления и рассылки</b>","COMPANY_ADMIN_EMPLOYEE_NOTIFICATIONS_OFF":"Пользователь {worker_login} <b>не будет получать системные уведомления и рассылки</b>","COMPANY_ADMIN_EMPLOYEE_ASSIGNED_ROLE":"Пользователю <b> {worker_login} </b> присвоена роль <b> {role_str} </b>","COMPANY_ADMIN_PUBLICATION_IS_SHOWN_NEWS_BLOCK":"Статья ({news_title}) <b>будет отображаться</b> в блоке «Новости» на странице компании","COMPANY_ADMIN_PUBLICATION_IS_HIDDEN_NEWS_BLOCK":"Статья ({news_title}) <b>не будет отображаться</b> в блоке «Новости» на странице компании","TITLE":"Заголовок","XPANEL_REFRESH_BUTTON_TITLE_ATTR":"Обновить комментарии","POST_PUBLISH":"Опубликовать","POST_SANDBOX_PUBLISH":"Отправить на модерацию","POST_READY_PUBLISH":"Далее к настройкам","POST_SAVED_DRAFT":"У вас есть автосохранение","POST_FROM":"от","SAVED_LOCALLY":"Сохранено в","POST_RESTORE":"Восстановить","POST_DRAFT":"В черновик","POST_BACK_TO_PUBLICATION":"Назад к публикации","POST_SETTINGS":"Настройки публикации","POST_TYPE":"Тип публикации","POST_TYPE_ARTICLE":"Статья","POST_TYPE_NEWS":"Новость","POST_TYPE_VOICE":"Голос свыше","POST_LANG":"Язык публикации","POST_LANG_RU":"Русский","POST_LANG_EN":"Английский","POST_HUBS":"Хабы","POST_HUBS_PLACEHOLDER":"Выберите хабы","POST_HUBS_HINT":"Выберите от 1 до 5 хабов по теме публикации","POST_TAGS":"Метки","POST_KEYWORDS":"Ключевые слова","POST_CUT_BUTTON_TEXT":"Текст кнопки «Читать далее»","POST_CUT_BUTTON_TEXT_DEFAUlT":"Читать далее","POST_CUT_BUTTON_TEXT_DEFAUlT_RU":"Читать далее","POST_CUT_BUTTON_TEXT_DEFAUlT_EN":"Read more","POST_TAGS_HINT":"Метки необходимо разделять запятой","POST_KEYWORDS_HINT":"Введите сюда от 1 до 10 ключевых слов, отделяя их запятыми.","POST_TRANSLATION":"Переведённый материал","POST_TRANSLATION_AUTHOR":"Автор оригинала","POST_TRANSLATION_AUTHOR_HINT":"Например: Tim O\'Reily","POST_TRANSLATION_SOURCE":"Ссылка на оригинальную публикацию","POST_TUTORIAL":"Tutorial","POST_TRANSLATION_SOURCE_HINT":"Например: http://www.oreillynet.com/pub/a/oreilly/tim/news/2005/09/30/what-is-web-20.html?page=1","POST_TEXT_BEFORE_CUT":"Аннотация","POST_TEXT_BEFORE_CUT_HINT":"Рекомендуем не меньше 100 и не более 2000 символов","POST_SOCIAL_COVER":"Обложка публикации","POST_SOCIAL_COVER_HINT":"Формат: jpg, gif, png","POST_MODERATED":"Проверено","POST_HUBS_CORPORATIVE":"Корпоративные","POST_HUBS_THEMATIC":"Тематические","POST_HUBS_OFFTOPIC":"Оффтопики","POST_HUBS_POST":"Уже добавлены в пост","POLLS_SUBJECT_PLACEHOLDER":"Тема опроса","POLLS_SUBJECT":"Тема","POLLS_VARIANTS":"Варианты ответа","POLLS_VARIANT_PLACEHOLDER":"Вариант","POLLS_VARIANTS_MULTIPLE":"Выбор нескольких вариантов","POLLS_VARIANTS_TIMED":"Ограниченное время голосования","POLLS_VARIANTS_ADD":"Добавить вариант","POLLS_ADD_POLL":"Добавить опрос","POLLS_DELETE_POLL":"Удалить опрос","POST_FEED_VIEW":"Отображение публикации в ленте","POST_ADD_COVER":"Добавьте обложку","POST_ADD_COVER_DESCRIPTION":"Перенесите сюда файл (jpg, gif, png) размером 780×440 или нажмите","PLAN_PUBLICATION":"Запланировать публикацию"}')
}, function (t) {
    t.exports = JSON.parse('{"ABUSE_SUBMITTED":"Thanks, the report has been sent","ACCOUNT_FORM_SETTING_SUCESS_MESSAGE":"New settings successfully applied","APP_WAS_SUBMITTED":"Your application has been submitted! You will be contacted shortly.","ATTENTION_MESSAGE":"Pay attention please","BETA_COOKIE_ENABLE_ALERT":"Enable cookie for testing","BETA_COOKIE_ENABLE":"Enable update","BETA_COOKIE_ENABLED":"Update enabled","CAPTCHA_IMAGE_MESSAGE":"You should enter 6 symbols from picture","CLOSE_BUTTON_TEXT":"Close","CLOSE_ALL_NOTICES":"Close all","COMMENT_ABUSE_CONFIRM":"Are you sure you want to report a comment?","COMMENT_ABUSE_NOTICE":"Thank you, the report has been sent","COMMENT_FAVORITE_ADDED":"Comment added to bookmarks","COMMENT_FAVORITE_REMOVED":"Comment removed from bookmarks","COMMENT_HIDDEN":"Comment was hidden","COMMENT_NOTICE_TEXT_REQIRED":"You can’t post an empty comment","COMMENT_REMOVE_CONFIRM":"Are you sure you want to delete it?","COMMENT_WAS_EDITED_TITLE":"Comment was edited","COMMENTS_COUNTDOWN_TIMER_LEFT":"Time expired","COMMENTS_REMOVED_HIDE":"Hide deleted comments","COMMENTS_REMOVED_SHOW":"Show deleted comments","COMMENTS_SETTINGS_SAVED_NOTICE":"Notification settings for new comments successfully saved","COMPANY_DONT_MEMBER":"You do not work for this company","COMPANY_MEMBER_I":"I work for this company","COMPANY_MEMBER_TOU":"You work for this company","COMPANY_QUIT_ACTION":"Leave the company","CONVERSATION_REMOVE_NOTICE":"Are you sure, you want to delete the converstion?","CONVERSATION_INVITE_USER":"Are you sure, you want to invite this user?","DRAFT_REMOVE_ACTION_ALERT":"Are you sure, you want to delete the draft?","FAVORITES_ADD_TITLE_ATTR":"Add to bookmarks","FAVORITES_REMOVE_TITLE_ATTR":"Remove from bookmarks","FEEDBACK_FORM_SUCESS_MESSAGE":"Your message has been sent. Be patient, you will receive an answer soon.","FIELD_VALIDATION_ERROR_FIELD_TYPE":"Field type","FIELD_VALIDATION_ERROR_LENGTH":"Message lenght","FIELD_VALIDATION_ERROR_MAXLENGTH":"Maximum characters amount","FIELD_VALIDATION_ERROR_MINLENGTH":"Minimal characters amount","FIELD_VALIDATION_ERROR_REQUIRED_KEYWORDS":"You should fill in the keywords that characterize your publication","FIELD_VALIDATION_ERROR_REQUIRED":"Required field","FORM_NOTICE_IMAGE_REMOVED":"Image deleted","FORM_NOTICE_UPLOAD_FAILED":"Upload failed","FORM_NOTICE_UPLOAD_SUCESSFULL":"File uploaded successfully","FORM_SELECT_CITY_PLACEHOLDER":"City","FORM_SELECT_REGION_PLACEHOLDER":"Region","FORM_UPLOAD_BUTTON_TEXT":"Select a file to upload","FORM_UPLOAD_REMOVE_BUTTON_TEXT":"delete or edit","FORM_VALIDATION_ERROR_MESSAGE":"Check all the fields are filled in correctly","FORM_SETTINGS_WAS_SAVED_SUCCESSFULL":"Successfully saved","GENERATED_ROWS_REMOVE_LINK":"Remove link","HABRACUT_TEXTLENGTH_WARNING":"Your post contains {length} characters. You must use \\"cut\\" tag if your post contains more than 1000 characters.","HUB_REQUEST_NOT_FOUND":"Could not find hub on request","INSTALLED_APPS_EMPTY_MESSAGE":"You don\'t have any applications installed","INVITE_GIVE_COUNTER":"You\'ve given an invitation.<br> You have {invites_string} invitations left.","INVITE_WAS_ACTIVED":"Invitation activited","INVITE_WAS_REVOKED":"Invitation revoked","INVITE_WAS_SENT_AGAIN":"Invitation was sent again","INVITE_WAS_SENT":"Invitation sent","KARMA_VOTES_COUNT_ALL":"Total {votes_count}","COPIED":"Copied to the clipboard","COPY_TEX_CODE":"Copy TeX code","LANGUAGE_FORM_SUCCESS_MESSAGE":"Settings successfully applied","MODERATION_POST_CONFIRM_MESSAGE":"Are you sure, you want to send this text to be checked?","NOTICE_SAVED":"The note has been saved","NOTICE_DELETED":"The note has been deleted","PASSWORD_HAS_BEEN_CHANGED":"Password changed","PASSWORD_RESTORE_LINK_MAIL":"Password recovery link sent to email address you\'ve specified during the registration.","PASSWORD_WAS_CHANGED_SUCCESSFULL":"Password successfully changed. Now you can <a href=\\"/login/\\">log in</a> using new password.","POST_ACTION_FAVORITES_ADDED":"The post added to bookmarks","POST_ACTION_FAVORITES_REMOVED":"The post removed from bookmarks","POST_PUBLISH_ACTION_ALERT":"Are you sure you want to publish this post?","POST_FORM_HAS_ERRORS":"Form has errors, please check all the fields and try again","POST_FORM_UNKONWN_ERROR":"Unknown error, please contact support","POSTS_TYPO_FORM_SUCCESS_SEND":"A message about error is sent","POST_VIEWS_COUNT_LABEL":"Views","PPA_WITHDRAWAL_FUNDS_DISABLED":"Automatic withdrawal disabled","PPA_WITHDRAWAL_FUNDS_ENABLED":"Automatic withdrawal enabled","QUESTION_REMOVE_CONFIRM":"Are you sure, you want to delete the question?","QUIZ_POLL_FAILED_NOTICE":"Unfortunately, you made a mistake in one of the answers. Try again","QUIZ_POLL_SUCESS_NOTICE":"Congratulations, you are with us now. Welcome","REJECT_REASON_DOESNT_CHOISE":"You did not choose the reason for rejecting the publication!","REMOVE_ACTION_BUTTON_TEXT":"delete","REQUEST_WAS_SUBMITTED_MODERATION":"Your application was sent for moderation","REVOKE_INVITE":"Revoke invitation","SAVE_SETTINGS":"Save settings","SCROLL_DOWN":"Down","SCROLL_TOP":"Up","SELECT_DEFAUL_OPTION_HUB":"Hub","SEMINAR_PAGE_TITLE":"Habr / Seminars","SETTINGS_LANGUAGE_INTERFACE":"Interface","SETTINGS_LANGUAGE_PUBLICATIONS":"Language of the posts","SETTINGS_LANGUAGE":"Language settings","SETTINGS_LANGUAGE_FEED_RU":"Russian","SETTINGS_LANGUAGE_FEED_EN":"English","SETTINGS_LANGUAGE_ERROR_MESSAGE":"You must select at least one language","SHOW_MORE_BUTTON_TEXT":"Show more","SOCIAL_PAGE_CONNECT_ACTION":"Connect","SUBSCRIBE_BUTTON_TEXT":"Subscribe","SUBSCRIBED_BUTTON_TEXT":"Subscribed","FOLLOW_BUTTON_TEXT":"Follow","UNFOLLOW_BUTTON_TEXT":"Unfollow","FOLLOWING_BUTTON_TEXT":"Following","TOGGLE_BRANCH":"Show comments","TOPIC_FORM_AUTOSAVE_INFO":"You have an autosaved draft on {date}, should we <a href=\\"#reset\\" class=\\"restore_form_data dotted\\">restore it</a>?","TOPIC_FORM_IMAGE_SRC_PROMPT":"Enter image src","TOPIC_FORM_LINK_URL_PROMPT":"Enter URL","TOPIC_FORM_PREVIEW_MODE_MESSAGE":"You are in the preview mode","TOPIC_FORM_SINGLETON_NOTICE":"You can not choose other hubs together with the hub \\"I\'m advertising\\".","TOPIC_FORM_SPOILER_TITLE":"Spoiler header","TOPIC_FORM_USER_NICKNAME":"Enter username","UNSUBSCRIBE_BUTTON_TEXT":"Unsubscribe","UPLOAD_FORM_PROGRESS":"Uploaded {received} of {size}","USER_DEFAULT_DESCRIPTION":"User","USER_SENT_PIS":"User sent to SRP","USER_SENT_PPG":"User sent to LP","VOTE_ACTION_MINUS":"You rated it negative","VOTE_ACTION_PLUS":"You rated it positively","VOTES_COUNTS_TITLE_ATTR":"Total votes {votes_count}: &uarr; {votes_count_plus} and &darr; {votes_count_minus}","XPANEL_COUNTER_TITLE_ATTR":"Number of new comments. Click (hotkey - f) leads to the first unread comment.","XPANEL_NEXT_BUTTON_TITLE_ATTR":"Click (hotkey - j) leads to the next unread comment.","XPANEL_PREV_BUTTON_TITLE_ATTR":"Click (hotkey - k) goes to the previous unread comment.","COMPANY_ADMIN_PUBLICATION_SHOWN":"The post <strong>will be shown</strong> in the corporate blog","COMPANY_ADMIN_PUBLICATION_HIDDEN":"The post <strong>will not be shown</strong> in the corporate blog","COMPANY_ADMIN_PLAN_NOTICE":"The subscription plan of the company\'s card will be changed after the expiration of the current subscription","COMPANY_ADMIN_BRANDING_CLEAR_NOTIFICATION":"Are you sure you want to delete branding decor?","COMPANY_ADMIN_FAVICON_WAS_UPDATE":"Favicon updated","COMPANY_ADMIN_REQUEST_WAS_APPROVED":"Thank you, your application has been accepted. <br/> An invoice-offer was sent to your e-mail. (check the \\"Spam\\" folder if you didn\'t received it); you can also find all financial documents in the \\"Finance\\" section of your blog. <br /> We well confirm the payment next day after it and send you an activation code.","COMPANY_ADMIN_STARTUP_REQUEST_WAS_APPROVED":"Заявка принята! После модерации мы отправим письмо с кодом активации блога на вашу почту. Этот код нужно будет вставить в разделе Код активации.","COMPANY_ADMIN_CURRENT_PROPS_WAS_EDIT":"Current requisites were edited","FORM_INFO_WAS_SAVED":"Successfully saved","COMPANY_ADMIN_VACANCY_HIDDEN":"Vacancy <b>will not be</b> shown in a company\'s card","COMPANY_ADMIN_VACANCY_SHOWN":"Vacancy <b>will be</b> shown in a company\'s card","COMPANY_ADMIN_WIDGET_WAS_PUBLISHED":"Widget is published on the website","COMPANY_ADMIN_WIDGET_WAS_HIDE":"Widget is hidden","COMPANY_ADMIN_APP_WIDGET_WAS_PUBLISHED":"App widget is published on the website","COMPANY_ADMIN_APP_WIDGET_WAS_HIDE":"App widget is hidden","COMPANY_ADMIN_WIDGET_SETTINGS_WERE_SAVED":"Widget settings successfully saved","COMPANY_ADMIN_EMPLOYEE_ROLE_DISABLED":"employee","COMPANY_ADMIN_EMPLOYEE_ROLE_AUTHOR":"author","COMPANY_ADMIN_EMPLOYEE_ROLE_WORKER":"Editor","COMPANY_ADMIN_EMPLOYEE_ROLE_ADMIN":"Admin","COMPANY_ADMIN_EMPLOYEE_REMOVE_CONFIRM":"Are you sure you want to delete this employee?","COMPANY_ADMIN_EMPLOYEE_NOTIFICATIONS_ON":"User {worker_login} <b>will receive system notifications and mailing</b>","COMPANY_ADMIN_EMPLOYEE_NOTIFICATIONS_OFF":"User {worker_login} <b>will not receive system notifications and mailing</b>","COMPANY_ADMIN_EMPLOYEE_ASSIGNED_ROLE":"User <b> {worker_login} </b> is now <b> {role_str} </b>","COMPANY_ADMIN_PUBLICATION_IS_SHOWN_NEWS_BLOCK":"This post ({news_title}) <b>will be shown</b> in the \\"News\\" block on a company\'s page","COMPANY_ADMIN_PUBLICATION_IS_HIDDEN_NEWS_BLOCK":"This post ({news_title}) <b>will not be shown</b> in the \\"News\\" block on a company\'s page","TITLE":"Header","XPANEL_REFRESH_BUTTON_TITLE_ATTR":"Refresh comments","POST_PUBLISH":"Publish","POST_SANDBOX_PUBLISH":"Send to moderation","POST_READY_PUBLISH":"Proceed to settings","POST_SAVED_DRAFT":"You have an autosaved draft","POST_FROM":"on","SAVED_LOCALLY":"Saved at","POST_RESTORE":"Restore","POST_DRAFT":"Save draft","POST_BACK_TO_PUBLICATION":"Back to publication","POST_SETTINGS":"Post settings","POST_TYPE":"Type","POST_TYPE_ARTICLE":"Article","POST_TYPE_NEWS":"News","POST_TYPE_VOICE":"Voice from above","POST_LANG":"Language","POST_LANG_RU":"Russian","POST_LANG_EN":"English","POST_HUBS":"Hubs","POST_HUBS_PLACEHOLDER":"Choose hubs","POST_HUBS_HINT":"Choose from 1 up to 5 hubs","POST_TAGS":"Tags","POST_KEYWORDS":"Keywords","POST_TAGS_HINT":"Comma separated","POST_KEYWORDS_HINT":"Enter from 1 to 10 key words here, separated by commas.","POST_TRANSLATION":"Translation","POST_CUT_BUTTON_TEXT":"Read more button text","POST_CUT_BUTTON_TEXT_DEFAUlT":"Read more","POST_CUT_BUTTON_TEXT_DEFAUlT_RU":"Читать далее","POST_CUT_BUTTON_TEXT_DEFAUlT_EN":"Read more","POST_TRANSLATION_AUTHOR":"Original author","POST_TRANSLATION_AUTHOR_HINT":"E.g. Tim O\'Reily","POST_TRANSLATION_SOURCE":"Original source","POST_TUTORIAL":"Tutorial","POST_TRANSLATION_SOURCE_HINT":"E.g. http://www.oreillynet.com/pub/a/oreilly/tim/news/2005/09/30/what-is-web-20.html?page=1","POST_TEXT_BEFORE_CUT":"Abstract","POST_TEXT_BEFORE_CUT_HINT":"We reccomend 100-2000 symbols","POST_SOCIAL_COVER":"Lead cover","POST_SOCIAL_COVER_HINT":"Ext: jpg, gif, png","POST_MODERATED":"Checked","POST_HUBS_CORPORATIVE":"Corporate","POST_HUBS_THEMATIC":"Thematic","POST_HUBS_OFFTOPIC":"Offtopic","POST_HUBS_POST":"Already set in post","POLLS_SUBJECT_PLACEHOLDER":"Specify poll subject","POLLS_SUBJECT":"Subject","POLLS_VARIANTS":"Users can select:","POLLS_VARIANT_PLACEHOLDER":"Answer","POLLS_VARIANTS_MULTIPLE":"Several answers","POLLS_VARIANTS_TIMED":"The poll has a validity period","POLLS_VARIANTS_ADD":"Add answer","POLLS_ADD_POLL":"Add poll","POLLS_DELETE_POLL":"Delete poll","POST_FEED_VIEW":"Post feed view","POST_ADD_COVER":"Add cover here","POST_ADD_COVER_DESCRIPTION":"Drop a file with 780×440 size here (jpg, gif, png) or click","PLAN_PUBLICATION":"Schedule the post"}')
}, function (t, e, o) {
    var n = o(7);
    void 0 !== t.exports && (t.exports = {
        showCommentParents: function t(e) {
            var o = n.findCommentById(e), r = function (t) {
                return $("> .parent_id", t).data("parent_id") || $("> .comment > .parent_id", t).data("parent_id")
            }(o);
            $("#reply_comments_" + r).removeClass("hidden"), o.show(), r && t(r)
        }
    })
}, function (t, e, o) {
    var n = {BTN_LOADING: "loading", FORM_HIDDEN: "hidden", BRANCH_FORM: "comment__reply-form"},
        r = {CURRENT_FORM: "#comments_form", FORM_PLACEHOLDER: ".js-form_placeholder"};
    void 0 !== t.exports && (t.exports = {
        clearInputField: function (t) {
            $('textarea[name="text"]', t).val("")
        }, hideForm: function (t) {
            t.closest(r.FORM_PLACEHOLDER).addClass(n.FORM_HIDDEN)
        }, hidePreview: function (t) {
            $("#preview_placeholder").addClass("hidden")
        }, isRootCommentsForm: function (t) {
            return !t.closest(r.FORM_PLACEHOLDER).hasClass(n.BRANCH_FORM)
        }, showForm: function () {
            $(r.CURRENT_FORM).closest(r.FORM_PLACEHOLDER).removeClass(n.FORM_HIDDEN)
        }, toggleLoadingState: function (t, e, o) {
            (e || $("button", t)).toggleClass("loading", o)
        }, updateTimestamp: function (t, e) {
            $('input[name="ts"]', t).val(e)
        }
    })
}, function (t, e, o) {
    var n = o(30), r = o(7), s = o(36), a = n.endTimer, i = s.listenCommentFormTitle, c = r.setCommentForm,
        l = r.showCommentButtons, u = window.mention_autocomplete;
    void 0 !== t.exports && (t.exports = {
        comment_show_reply_form: function t(e, o) {
            a();
            var n = $("#comment_" + e), r = $("#comment-form");
            if (r.length) e ? n.children(".js-form_placeholder").html(r) : $("#comments_form_placeholder").append(r), n.children(".js-form_placeholder").removeClass("hidden"), c(r, e, 0, "", o), l("reply"), r.find(".time_left").text(""), u($("#comment_text")), i(Boolean(e), t); else {
                var s = $("#expired_placeholder");
                $("#comment_" + e + " .js-form_placeholder").html(s), s.removeClass("hidden")
            }
            return !1
        }
    })
}, function (t, e, o) {
    var n = window.timer_countdown, r = {element: null, left: 0, timer: null};

    function s(t) {
        null !== r.timer && (clearInterval(r.timer), a(t ? window.i18nMsg("COMMENTS_COUNTDOWN_TIMER_LEFT") : ""))
    }

    function a(t) {
        r.element && r.element.text(t.toString())
    }

    void 0 !== t.exports && (t.exports = {
        endTimer: s, startTimer: function (t, e, o) {
            r.element = t, r.left || (r.left = e), r.timer && s(), r.timer = setInterval(function () {
                r.left > 0 ? (r.left -= 1, a(n(r.left))) : s(!0)
            }, o)
        }
    })
}, , function (t, e) {
    Suggest = function (t) {
        "use strict";
        this.validateSettings(t) && (this._settings = t, this._clearButton = null, this._suggestElement = null, this._suggestWrapper = null, this._debounceTimer = !1, this._value = "", this.init())
    }, Suggest.prototype.validateSettings = function (t) {
        return !!t.selectors && !!(t.selectors.clear && t.selectors.container && t.selectors.suggest)
    }, Suggest.prototype.init = function () {
        "use strict";
        this._suggestElement = $(this._settings.selectors.suggest), this._suggestWrapper = this._suggestElement.parent(), this._clearButton = $(this._settings.selectors.clear);
        var t = $(this._settings.selectors.container);
        this._fuzzySearch = function (t, e) {
            var o = e, n = $(".page__footer"), r = t.attr("data-type"), s = o.html();
            return {
                reset: function () {
                    o.html(s), n.show()
                }, fetch: function (t, e) {
                    var s = {query: t, type: r};
                    $.get("/json/suggest/", s, function (t) {
                        "ok" === t.messages ? (n.hide(), o.html(t.html), e && e(null, t.html)) : (formUtils.showSystemError(t), e && e(t, null))
                    }, "json")
                }
            }
        }(this._suggestElement, t), this._clearButton.on("click", $.proxy(this.onClearClick, this)), this._suggestElement.on("change keyup", $.proxy(this.onElementChange, this))
    }, Suggest.prototype.onClearClick = function () {
        "use strict";
        this.clear()
    }, Suggest.prototype.onElementChange = function (t) {
        "use strict";
        27 === (t.charCode || t.keyCode) && this.clear();
        var e = this._suggestElement.val();
        this._value !== e && (this._value = e, this.enableLoadingState(!0), this.enableFilledState(!1), this._debounce($.proxy(function () {
            this.fetchResults(e)
        }, this)))
    }, Suggest.prototype.clear = function () {
        "use strict";
        this._suggestElement.val(""), this._suggestElement.trigger("change"), this.enableFilledState(!1)
    }, Suggest.prototype._debounce = function (t) {
        "use strict";
        this._debounceTimer && clearTimeout(this._debounceTimer), this._debounceTimer = setTimeout($.proxy(t, this), 300)
    }, Suggest.prototype.fetchResults = function () {
        "use strict";
        this._value ? this._fuzzySearch.fetch(this._value, $.proxy(function () {
            this.enableLoadingState(!1), this.enableFilledState(!0)
        }, this)) : (this._fuzzySearch.reset(), this.enableFilledState(!1), this.enableLoadingState(!1))
    }, Suggest.prototype.enableLoadingState = function (t) {
        this._suggestWrapper.toggleClass("loading", t)
    }, Suggest.prototype.enableFilledState = function (t) {
        this._suggestWrapper.toggleClass("filled", t)
    }, t.exports = Suggest
}, , function (t, e, o) {
    var n = o(2);

    function r(t) {
        var e = "." + t.attr("class").split(/\s+/).join("."), o = "";
        return $.each(t[0].attributes, function (t, e) {
            e.name && -1 !== e.name.indexOf("data-") && (o += "[" + e.name + '="' + e.value + '"]')
        }), e + o
    }

    Follow = function (t) {
        "use strict";
        this.validateSettings(t) && (this._settings = t, this.init())
    }, Follow.prototype.validateSettings = function (t) {
        return !!t.selectors && (!!t.selectors.follow && (!!t.suggestUrl && "function" == typeof t.suggestRequest))
    }, Follow.prototype.init = function () {
        "use strict";
        $(document).on("click", this._settings.selectors.follow, $.proxy(this.onFollowButtonClick, this))
    }, Follow.prototype.onFollowButtonClick = function (t) {
        "use strict";
        var e = t.currentTarget;
        this.followUserByButton(e)
    }, Follow.prototype.followUserByButton = function (t) {
        "use strict";
        var e = this._settings.suggestUrl, o = this._settings.suggestRequest(t);
        !function (t, e, o) {
            var s = t.attr("data-state");
            if (document.querySelector(".author-panel")) var a = "author_info_bottom"; else a = "author_info_top";
            var i = t.find(".js-btn-value"), c = t.closest(".js-subscribe_item").find(".js-subscribed"),
                l = e + s + "/";
            $.post(l, o, function (e) {
                if ("ok" === e.messages) if ("unfollow" === s) {
                    "function" == typeof ga && ga("send", "event", a, "unsubscribe", o.login), $(r(t)).attr("data-state", "follow").removeClass("btn_subscribed").addClass("btn_subscribe");
                    var l = o.login ? window.i18nMsg("FOLLOW_BUTTON_TEXT") : window.i18nMsg("SUBSCRIBE_BUTTON_TEXT");
                    i.text(l), c.removeClass("is-subscribed")
                } else "follow" === s && ("function" == typeof ga && ga("send", "event", a, "subscribe", o.login), $(r(t)).attr("data-state", "unfollow").removeClass("btn_subscribe").addClass("btn_subscribed"), l = o.login ? window.i18nMsg("FOLLOWING_BUTTON_TEXT") : window.i18nMsg("SUBSCRIBED_BUTTON_TEXT"), i.text(l), c.addClass("is-subscribed")); else n.showSystemError(e)
            }, "json")
        }($(t), e, o)
    }, t.exports = Follow
}, function (t, e, o) {
    var n = o(47), r = o(7), s = o(28), a = o(23), i = o(66), c = null, l = null, u = null, d = null;

    function _(t) {
        var e = $(i.WRAPPER()), o = function (t, e) {
            var o = $(i.COUNTER(t));
            return o.on("click", e), o
        }(t, A), n = function (t) {
            var e = $(i.REFRESH_BUTTON());
            return e.on("click", t), e
        }(v), r = function (t) {
            var e = $(i.PREV_BUTTON());
            return e.on("click", t), e
        }(C), s = function (t) {
            var e = $(i.NEXT_BUTTON());
            return e.on("click", t), e
        }(O);
        return e.append([n, r, o, s]), 0 === t && (r.hide(), s.hide(), o.hide()), c = o, l = s, u = r, d = n, e, e
    }

    function m() {
        var t = $(".js-comment"), e = $(".js-comment_tree");
        t.show(), e.removeClass("open")
    }

    function f() {
        return $(".js-comment_new")
    }

    function p(t) {
        $(t).removeClass("js-comment_new").find(".comment__head_new-comment").eq(0).removeClass("comment__head_new-comment")
    }

    function h() {
        return $("#comments_form")
    }

    function g(t) {
        c && c.text(t)
    }

    function E(t) {
        var e = $([c[0], l[0], u[0]]);
        t ? e.show() : e.hide()
    }

    function T(t) {
        n.appendNewComments(t);
        var e = t.length;
        g(e), n.addCountForAllComments(e), E(Boolean(e)), d.removeClass("loading"), a.instagrammProcess()
    }

    function v(t) {
        var e, o, n, r = $(t.currentTarget), a = $([c[0], l[0], u[0]]).is(":visible") ? "renew" : "refresh";
        r.hasClass("loading") || (N(a, window.userLabel), m(), f().toArray().forEach(p), e = T, o = h(), n = "/json/comment/", "beta" === o.attr("data-type") && (n = "/json/beta/new-comments/"), o.length ? o.ajaxSubmit({
            data: {action: "get_new"},
            url: n,
            success: function (t) {
                s.updateTimestamp(h(), t.ts), e(t.comments)
            }
        }) : e([]), r.addClass("loading"))
    }

    function b(t, e) {
        return f().toArray().reduce(function (o, n) {
            var r = $(n), s = -100 * e;
            return (r.offset().top + s - t) * e > 0 ? o.concat(r) : o
        }, [])
    }

    function S() {
        w($(".js-comment .comment.is_selected"), !1)
    }

    function w(t, e) {
        t.toggleClass("is_selected", e)
    }

    function O(t) {
        var e, o = b(window.pageYOffset, 1)[0];
        S(), o && o.length ? (w($(".comment", o).first(), !0), r.scrollToComment(o)) : (e = f().first(), r.scrollToComment(e)), N("next", window.userLabel)
    }

    function C(t) {
        var e, o = b(window.pageYOffset, -1), n = o[o.length - 1];
        S(), n && n.length ? (w($(".comment", n).first(), !0), r.scrollToComment(n)) : (e = f().last(), r.scrollToComment(e)), N("prev", window.userLabel)
    }

    function A(t) {
        m();
        var e = f(), o = e.length;
        if (o) {
            var n = e[0];
            o -= 1, r.scrollToComment(n, function () {
                setTimeout(function () {
                    p(n)
                }, 300)
            })
        }
        g(o), o || E(!1), N("decrement", window.userLabel)
    }

    function N(t, e) {
        "function" == typeof ga && ga("send", "event", "xpanel", t, e)
    }

    void 0 !== t.exports && (t.exports = {
        initXPanel: function () {
            if ($("#comments").length) {
                var t = _(f().length);
                $("body").append(t)
            }
        }, syncCommentCountFromDOM: function () {
            var t = f().length;
            g(t), t && E(!0)
        }
    })
}, function (t, e, o) {
    void 0 !== t.exports && (t.exports = {
        listenCommentFormTitle: function (t, e) {
            var o = $(".comment-form__title"), n = o.data("has-listener");
            if (t) {
                if (n) return;
                o.on("click", function () {
                    e()
                })
            } else {
                if (!n) return;
                o.off("click")
            }
            o.toggleClass("comment-form__title_listened", t), o.data("has-listener", t)
        }
    })
}, , , , function (t, e, o) {
    $(document).ready(function () {
        "use strict";
        $.jGrowl.defaults.closerTemplate = "<div>" + window.i18nMsg("CLOSE_ALL_NOTICES") + "</div>", $(".help-tip").length && $(".help-tip").tipTip({
            maxWidth: "auto",
            edgeOffset: 10
        }), $(".sidebar_content-area").length && $(".sidebar_content-area").addClass("sticky_init")
    });
    var n;
    window.addEventListener("message", t => {
        t.data && t.data.type && "embed-size" === t.data.type && (n = t.data, document.getElementById(n.id).height = n.height)
    }), $(window).load(function () {
        var t = location.hash;
        r(t), !!(!window.history || !history.pushState) || "#habracut" !== document.location.hash && "#comments" !== document.location.hash && "#first_unread" !== document.location.hash || history.replaceState({}, document.title, document.location.pathname + document.location.search)
    });
    var r = function (t) {
        if (t) {
            var e = $(".main-navbar").height();
            (t => {
                try {
                    document.createDocumentFragment().querySelector(t)
                } catch (t) {
                    return !1
                }
                return !0
            })(t) && document.querySelector(t) || (t = `a[name="${t.slice(1)}"]`), setTimeout(() => {
                $.scrollTo(t, 400, {axis: "y", offset: -1 * e - 4})
            }, 0)
        }
    };

    function s(t) {
        if (void 0 !== t.errors) {
            for (var e in t.errors) {
                var o = e.replace(/[\]\[]/g, ""), n = t.errors[e];
                Array.isArray(n) && (n = n.map(function (t) {
                    return "<p>" + t + "</p>"
                }).join(""));
                var r = $("." + o + " .error");
                0 !== r.length ? r.html(n).fadeIn() : 0 !== (r = $('[name="' + e + '"]')).length && r.parent().find(".error").show().html(n)
            }
            $.scrollTo($(".error:visible").first(), 800, {axis: "y", offset: -150})
        }
        u(t)
    }

    $("a").click(function (t) {
        var e = $(this).attr("href"), o = $(this).hasClass("icon_comment-anchor"),
            n = $(this).hasClass("toggle-menu__item-link");
        !e || "#" === e || "#" !== e[0] || o || n || (t.preventDefault(), r(e), window.history.pushState({}, "", e))
    }), $(document).ready(function () {
        function t(t) {
            if (!t) {
                var e = $(".dfp-slot__banner");
                if (e.length) {
                    var o = "undefined" == typeof adblockInit;
                    e.toggleClass("dfp-slot__banner_hidden", o)
                }
            }
        }

        function e(t) {
            var e = t.closest(".spoiler");
            e.hasClass("spoiler_open") && $("iframe", e).attr("src", function (t, e) {
                return e
            }), $("> .spoiler_text", e).toggle(), e.toggleClass("spoiler_open")
        }

        window.ajax_errors_count = 0, window.habr_blockers_checker ? window.habr_blockers_checker.detectWrapper(function (e) {
            t(e)
        }) : t(!1), $(document).on("click", ".subscribeUser", d), $(document).on("click", ".unsubscribeUser", _), $(document).on("submit", 'form[data-remote="true"]', function () {
            var t = $(this);
            return t.ajaxSubmit({
                form: t, beforeSubmit: a, error: i, success: c(function (t) {
                    void 0 !== t.message && $.jGrowl(t.message)
                })
            }), !1
        }), $(document).on("change", "input, textarea", function () {
            $(this).parent().find(".error").hide().html("")
        }), $(document).on("click", 'a[data-remote="true"]', function (t) {
            for (var e = $(this), o = {}, n = 0, r = e.get(0).attributes, s = r.length; n < s; n++) if ((r.item(n).nodeName + "").indexOf("data-", 0) >= 0) {
                var a = r.item(n).nodeName;
                o[a = a.replace(/data-/g, "")] = $(this).attr("data-" + a)
            }
            var i = $.map(o, function (t, e) {
                return e + "=" + t
            }).join("&");
            $.post(e.attr("href"), i, function (t) {
            }), t.preventDefault()
        }), $(document).on("submit", ".digest_sub_form", function () {
            var t = $(this);
            return "subscription_close" === t[0].name && $(".subscription__thanks").is(":visible") ? ($(".subscription").hide(), !1) : ($(".subscription__form .btn").addClass("btn-loading"), t.ajaxSubmit({
                form: t,
                beforeSubmit: a,
                error: i,
                success: c(function (e) {
                    if (void 0 !== e.message && $.jGrowl(e.message), $(".subscription__form .btn").removeClass("btn-loading"), "subscription_close" === t[0].name) $(".subscription").hide(), "function" == typeof ga && ga("send", "event", "tm_block", "digest_feed", "close"); else {
                        var o = function (t) {
                            switch (t) {
                                case"day":
                                    return "subscribe_daily";
                                case"week":
                                    return "subscribe_weekly";
                                case"month":
                                    return "subscribe_monthly";
                                case"never":
                                    return "close";
                                default:
                                    return null
                            }
                        }($('select[name="frequency"]').val());
                        "function" == typeof ga && ga("send", "event", "tm_block", "digest_feed", o), $(".subscription__form").hide(), $(".subscription__thanks").show()
                    }
                })
            }), !1)
        }), $(".join_hub_panel .join_hub").on("click", function () {
            var t = $(this);
            t.addClass("loading");
            var e = $(this).attr("data-id"), o = $("#hub_item_" + e);
            return $.post("/json/hubs/subscribe/", {hub_id: e}, function (e) {
                "ok" == e.messages ? ($(".leave_hub", o).removeClass("hidden"), $(".join_hub", o).addClass("hidden"), o.addClass("membership")) : u(e), t.removeClass("loading")
            }, "json"), !1
        }), $(".join_hub_panel .leave_hub").on("click", function () {
            var t = $(this).attr("data-id"), e = $(this);
            e.addClass("loading");
            var o = $("#hub_item_" + t);
            return $.post("/json/hubs/unsubscribe/", {hub_id: t}, function (t) {
                "ok" == t.messages ? ($(".leave_hub", o).addClass("hidden"), $(".join_hub", o).removeClass("hidden"), o.removeClass("membership")) : u(t), e.removeClass("loading")
            }, "json"), !1
        }), $(document).ajaxError(function (t, e, o) {
            $("input.loading, button.loading").attr("disabled", !1).removeClass("loading"), window.ajax_errors_count++
        }), $(document).ajaxSuccess(function (t, e, o, n) {
            "json" == o.dataType && n && "string" == typeof n.debug && $("#php-debug").replaceWith(n.debug)
        }), $(document).on("click", ".spoiler_title", function () {
            e($(this))
        }), $(document).on("keydown", function (t) {
            if (13 == (t = t || window.event).which) {
                var o = $(document.activeElement);
                o.hasClass("spoiler") && e(o.find(".spoiler_title"))
            }
        }), $("#broadcast_tabs_comments, #broadcast_tabs_posts").length && ($("#broadcast_tabs_comments, #broadcast_tabs_posts").tabslet({animation: !0}), $("#broadcast_tabs_comments .toggle-menu__item-link").on("click", function (t) {
            if ($(t.target).hasClass("active")) return !1
        })), $(function () {
            $("a.js-smoothscroll").on("click", function () {
                if (location.pathname.replace(/^\//, "") == this.pathname.replace(/^\//, "") && location.hostname == this.hostname) {
                    var t = $(this.hash);
                    if ((t = t.length ? t : $("[name=" + this.hash.slice(1) + "]")).length) return $("html,body").animate({scrollTop: t.offset().top}, 1e3), !1
                }
            })
        }), $("#global_notify .close").on("click", function () {
            var t = $(this).attr("data-id"), e = $("#notification_" + t), o = e.next(".inner_notice");
            return e.remove(), o && o.removeClass("hidden"), $.post("/json/notifications/close/", {id: t}, function (t) {
                "ok" !== t.messages && u(t)
            }, "json"), !1
        })
    });
    var a = function (t, e, o) {
        $(".error", e).text("").hide(), $('input[type="submit"], input[type="button"], button[type="button"], button[type="submit"]', e).attr("disabled", !0)
    }, i = function (t, e) {
        $('input[type="submit"], input[type="button"], button[type="button"], button[type="submit"]', e).attr("disabled", !1).removeClass("loading")
    }, c = function (t, e, o) {
        return function (o, n, r, a) {
            if ($('input[type="submit"], input[type="button"], button[type="button"], button[type="submit"]', this.form).attr("disabled", !1).removeClass("loading"), "ok" == o.messages) {
                if (l(o)) return;
                return t(o, n, r, a)
            }
            s(o), "function" == typeof e && e(o, n, r, a)
        }
    }, l = function (t) {
        return void 0 !== t.redirect_debug ? ($.jGrowl('REDIRECT DEBUG: <a href="' + t.redirect_debug + '">' + t.redirect_debug + "</a>", {
            theme: "message",
            sticky: !0
        }), !0) : void 0 !== t.redirect && (document.location.href = t.redirect, !0)
    };

    function u(t) {
        if (function (t) {
            if (void 0 !== t.system_warnings) for (var e in t.system_warnings) $.jGrowl(t.system_warnings[e], {theme: "warning"})
        }(t), void 0 !== t.system_errors) for (var e in t.system_errors) $.jGrowl(t.system_errors[e], {theme: "error"})
    }

    function d() {
        var t = $(this), e = t.attr("data-login"), o = t.data("id");
        return t.addClass("loading"), $.post("/json/users/follow/", {login: e}, function (e) {
            "ok" == e.messages ? ($(".js-user_" + o + " .unsubscribeUser").removeClass("hidden"), $(".js-user_" + o + " .subscribeUser").addClass("hidden"), $(".js-user_" + o).addClass("subscribed")) : u(e), t.removeClass("loading")
        }, "json"), !1
    }

    function _() {
        var t = $(this), e = t.data("login"), o = t.data("id");
        return t.addClass("loading"), $.post("/json/users/unfollow/", {login: e}, function (e) {
            "ok" == e.messages ? ($(".js-user_" + o + " .unsubscribeUser").addClass("hidden"), $(".js-user_" + o + " .subscribeUser").removeClass("hidden"), $(".js-user_" + o).removeClass("subscribed")) : u(e), t.removeClass("loading")
        }, "json"), !1
    }

    function m(t) {
        return t < 10 ? "0" + t : t
    }

    var f = {ie: navigator.userAgent.match(/MSIE\s([^;]*)/)};
    $(document).on("mousedown", "a[data-utm]", function () {
        var t = $(this), e = t.attr("href") + t.data("utm");
        t.attr("href", e)
    }), $(document).on("click", ".informer > .close", function () {
        $(".informer").hide(), $.cookie("bttf", 1, {expires: 60, path: "/"})
    }), $(document).on("mousedown", "a[data-statistics-params]", function (t) {
        var e = $(this), o = e.attr("href").split("#")[0] + e.data("statistics-params") + "#comments";
        e.attr("href", o)
    }), void 0 !== t.exports && (t.exports = {
        ajaxFormBeforSubmit: a,
        ajaxFormError: i,
        ajaxFormRedirect: l,
        ajaxFormSuccess: c,
        createCookie: function (t, e, o) {
            var n = new Date, r = "";
            o ? (n.setTime(n.getTime() + 24 * o * 60 * 60 * 1e3), r = "; expires=" + n.toGMTString()) : (r = "", document.cookie = t + "=" + e + r + "; path=/")
        },
        empty: function (t) {
            return "" === (t = t.replace(/\s+/g, ""))
        },
        _getDate: function (t) {
            var e = t.getDate(), o = t.getMonth(), n = t.getFullYear();
            return e + " " + ["января", "февраля", "марта", "апреля", "мая", "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"][o] + " " + n
        },
        _getTime: function (t) {
            var e = t.getHours(), o = t.getMinutes();
            return o < 10 && (o = "0" + o), e + ":" + o
        },
        H: function (t) {
            var e = new Date, o = new Date(t), n = e - o, r = 864e5;
            return f.ie && (o = Date.parse(t.replace(/( \+)/, " UTC$1"))), isNaN(n) || n < 0 ? "" : n < 7e3 ? "right now" : n < 6e4 ? Math.floor(n / 1e3) + " s" : n < 12e4 ? "1 m" : n < 36e5 ? Math.floor(n / 6e4) + " m" : n < 72e5 ? "1 h" : n < r ? Math.floor(n / 36e5) + " h" : n > r && n < 2 * r ? "yesterday" : n < 31536e6 ? Math.floor(n / r) + " d" : "over a year ago"
        },
        K: f,
        mention_autocomplete: function (t) {
            t.textcomplete([{
                match: /(^|\s)@([-\w]*)$/, template: function (t) {
                    return '<img src="' + t.avatar + '" alt="" /> <span class="name">' + t.value + "</span>"
                }, search: function (t, e) {
                    t.length >= 3 ? $.getJSON("/json/mentions/", {term: t}).done(function (t) {
                        e(t.results)
                    }).fail(function () {
                        e([])
                    }) : e([])
                }, replace: function (t) {
                    return "$1@" + t.value + " "
                }
            }])
        },
        replaceURLWithHTMLLinks: function (t) {
            return t = (t = (t = t.replace(/(\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/i, '<a href="$1">$1</a>')).replace(/(^|\s)@(\w+)/g, '$1@<a href="https://www.twitter.com/$2">$2</a>')).replace(/(^|\s)#(\w+)/g, '$1<a href="https://twitter.com/search?q=%23$2">#$2</a>')
        },
        show_form_errors: s,
        show_system_error: u,
        timer_countdown: function (t) {
            days = Math.floor(t / 86400), hours = Math.floor(t / 3600), mins = Math.floor(t / 60), secs = Math.floor(t), dd = days, hh = hours - 24 * days, mm = mins - 60 * hours, ss = secs - 60 * mins;
            var e = [];
            return hh > 0 && e.push(hh ? m(hh) : "00"), e.push(mm ? m(mm) : "00"), e.push(ss ? m(ss) : "00"), e.join(":")
        },
        userKarmaVote: function (t) {
            var e = $(t);
            if (document.querySelector(".author-panel")) var o = "author_info_bottom"; else o = "author_info_top";
            var n = e.closest(".js-user-vote"), r = n.find(".js-score"), s = e.attr("data-action"),
                a = e.attr("data-login"), i = {
                    ti: n.attr("data-id"), tt: n.attr("data-type"), v: function (t) {
                        return "plus" == t ? "1" : "-1"
                    }(s)
                };
            return $.post("/json/vote/", i, function (t) {
                if ("ok" == t.messages) {
                    "function" == typeof ga && ga("send", "event", o, "vote_" + s, a);
                    var c = i.v, l = (Number(t.score), "positive" == t.sign ? "green" : "red"),
                        d = window.i18nMsg("KARMA_VOTES_COUNT_ALL", {votes_count: t.votes_count});
                    switch (n.find('button[type="button"]').attr("disabled", !1), n.find('button[type="button"]').removeClass("voting-wjt__button_plus voting-wjt__button_minus"), c) {
                        case"1":
                            e.attr({
                                title: window.i18nMsg("VOTE_ACTION_PLUS"),
                                disabled: !0
                            }), e.addClass("voting-wjt__button_plus");
                            break;
                        case"-1":
                            e.attr({
                                title: window.i18nMsg("VOTE_ACTION_MINUS"),
                                disabled: !0
                            }), e.addClass("voting-wjt__button_minus")
                    }
                    r.replaceWith('<div class="stacked-counter__value stacked-counter__value_' + l + '" title="' + d + '">' + t.score + "</div>")
                } else u(t)
            }, "json"), !1
        },
        copyCurrentUrl: function () {
            var t = document.createElement("input"), e = window.location.href;
            document.body.appendChild(t), t.value = e, t.select(), document.execCommand("copy"), document.body.removeChild(t), $.jGrowl(window.i18nMsg("COPIED"))
        }
    })
}, function (t, e, o) {
    var n = null;
    $(window).load(function () {
        $("form").each(function () {
            var t = $(this);
            $('input[type="submit"], input[type="button"]', t).on("click", function () {
                $(this).addClass("loading")
            }), $(".item.required", t).each(function (t, e) {
                $(e).hasClass("hubs") ? $("input#token-input-hubs", e).on("blur", function () {
                    0 == $('input[name="hubs"]', e).tokenInput("get").length ? $(".error", e).text(i18nMsg("FIELD_VALIDATION_ERROR_REQUIRED")).show() : $(".error", e).text("").hide()
                }) : $('input[type="text"], input[type="password"], textarea', e).on("blur", function (t) {
                    var o = $(this).val();
                    o = o.replace(/\s+/g, "");
                    var n = $(e).find('input[name="tags_string"]').length, r = "" === o,
                        s = i18nMsg("FIELD_VALIDATION_ERROR_REQUIRED"),
                        a = i18nMsg("FIELD_VALIDATION_ERROR_REQUIRED_KEYWORDS");
                    r ? n ? $(".error", e).text(a).show() : $(".error", e).text(s).show() : $(".error", e).text("").hide()
                })
            })
        });
        var t = $(".select_date");
        $(".country", t).on("change", function () {
            var e = $(".region", t), o = $(".city", t);
            e.attr("disabled", !0), o.attr("disabled", !0), $.get("/json/geo/regions/", {country_id: $(this).val()}, function (t) {
                for (k in e.html('<option value="0">' + i18nMsg("FORM_SELECT_REGION_PLACEHOLDER") + "</option>"), o.html('<option value="0">' + i18nMsg("FORM_SELECT_CITY_PLACEHOLDER") + "</option>"), t.regions) {
                    var n = t.regions[k];
                    e.append('<option value="' + n.id + '">' + n.name + "</option>")
                }
                e.attr("disabled", !1)
            }, "json")
        }), $(".region", t).on("change", function () {
            var e = $(".city", t);
            e.attr("disabled", !0), $.get("/json/geo/cities/", {region_id: $(this).val()}, function (t) {
                for (k in e.html('<option value="0">' + i18nMsg("FORM_SELECT_CITY_PLACEHOLDER") + "</option>"), t.cities) {
                    var o = t.cities[k];
                    e.append('<option value="' + o.id + '">' + o.name + "</option>")
                }
                e.attr("disabled", !1)
            }, "json")
        });
        var e = $(".iframe_uploader_preview .image");
        $(".delete", e).on("click", function () {
            var t = $(this).data("name");
            return $('input[name="' + t + '"]').val("-1"), $("." + t + " .iframe_uploader_preview").html('<div class="image">' + i18nMsg("FORM_NOTICE_IMAGE_REMOVED") + "</div>"), !1
        })
    }), $(document).ready(function () {
        $(".radio_list.custom").each(function (t, e) {
            $("label", e).each(function (t, o) {
                $('input[type="radio"]', o).each(function (t, n) {
                    $(n).attr("checked") && ($("label", e).removeClass("checked"), $(o).addClass("checked")), $(n).click(function () {
                        $(n).attr("checked") && ($("label", e).removeClass("checked"), $(o).addClass("checked"))
                    })
                })
            })
        }), $("textarea[maxlength], input[maxlength]").each(function (t, e) {
            if (!0 !== $(e).data("nocount")) {
                var o = $(this).parents(".item"), n = (e = $(e), i18nMsg("FIELD_VALIDATION_ERROR_MAXLENGTH")),
                    r = $('<div class="count" title="' + n + '">' + e.attr("maxlength") + "</div>");
                o.append(r), r.text(e.attr("maxlength") - e.val().length), e.bind("keyup blur focus change", function () {
                    r.text(e.attr("maxlength") - e.val().length)
                })
            }
        })
    }), void 0 !== t.exports && (t.exports = {
        RecaptchaOptions: {lang: "ru", theme: "clean"}, show_uploader: function (t) {
            var e = $(t), o = $('input[name="upload_id"]', e), r = e.offset();
            $("img", e).remove(), $("a", e).remove(), o.val(""), e.css("height", "60px");
            var s = $('<form action="/upload/" class="upload_form" method="post" enctype="multipart/form-data" accept="image/gif, image/jpeg"><input type="file" name="image" /><div class="progress"><div class="bar"></div></div><div class="state">' + i18nMsg("FORM_UPLOAD_BUTTON_TEXT") + "</div></form>");
            $("body").append(s), s.css({position: "absolute", left: r.left + 1, top: r.top + 1});
            var a = $('input[type="file"]', s), i = $(".bar", s), c = $(".state", s);
            return e.bind("reset_position", function () {
                r = e.offset(), s.css({position: "absolute", left: r.left + 1, top: r.top + 1})
            }), a.change(function () {
                var t = g_user_login + (new Date).getTime();
                s.ajaxSubmit({
                    dataType: "xml",
                    url: "/upload/?X-Progress-ID=" + t,
                    iframe: !0,
                    forceSync: !0,
                    resetForm: !0,
                    beforeSubmit: function (e) {
                        n = setInterval(function () {
                            $.get("/progress/", {"X-Progress-ID": t}, function (t) {
                                if ("uploading" == t.state) {
                                    var e = t.received / t.size * 100;
                                    i.css("width", e + "%"), c.html(window.i18nMsg("UPLOAD_FORM_PROGRESS", {
                                        received: t.received,
                                        size: t.size
                                    }))
                                } else "done" == t.state && clearInterval(n)
                            }, "json")
                        }, 500)
                    },
                    success: function (t) {
                        if (clearInterval(n), "ok" == $(t).find("message").text()) i.css({width: "0%"}), c.html(i18nMsg("FORM_NOTICE_UPLOAD_SUCESSFULL")), s.remove(), e.append('<img src="' + $(t).find("url").text() + '" alt=""  /><a href="#upload" class="upload_again" onclick="return show_uploader($(this).parent());">' + i18nMsg("FORM_UPLOAD_REMOVE_BUTTON_TEXT") + "</a>"), e.css("height", "auto"), o.val($(t).find("id").text()); else {
                            i.css({width: "0%"});
                            var r = $(t).find("error");
                            r ? r.each(function () {
                                c.html('<span class="error">' + $(this).text() + "</span>")
                            }) : c.html('<span class="error">' + i18nMsg("FORM_NOTICE_UPLOAD_SUCESSFULL") + "</span>")
                        }
                    }
                })
            }), !1
        }
    })
}, function (t, e, o) {
    var n = document.getElementById("search-form"), r = document.getElementById("search-form-btn"),
        s = document.getElementById("search-form-field"), a = document.getElementById("search-form-clear"),
        i = document.getElementById("navbar-links");

    function c() {
        n.className += " search-form_expanded", i.className += " hidden", s.focus()
    }

    function l() {
        s.value = "", s.blur(), n.classList.remove("search-form_expanded"), i.classList.remove("hidden")
    }

    r.onclick = function (t) {
        t.preventDefault(), c()
    }, a.onclick = function (t) {
        t.preventDefault(), l()
    }, s.onfocus = function (t) {
        document.onkeydown = function (t) {
            27 == (t = t || window.event).which && l()
        }
    }, void 0 !== t.exports && (t.exports = {openSearch: c})
}, function (t, e, o) {
    var n = o(34), r = o(32);
    $(document).ready(function () {
        "use strict";
        new r({
            selectors: {
                clear: "#people_suggest_clear",
                container: "#peoples",
                suggest: "#people_suggest"
            }
        }), new n({
            selectors: {follow: ".js-user-follow-btn"},
            suggestUrl: "/json/users/",
            suggestRequest: function (t) {
                return {login: $(t).attr("data-login")}
            }
        })
    })
}, function (t, e, o) {
    !function () {
        function t() {
            var t = $("#mdCheck, #comment_markdown");
            if (t) return t.is(":checked")
        }

        var e = $(".tm-editor__textarea").get(0);
        $(document).on("keydown", function (t) {
            if (e) {
                var o = habraWYG.getCursor(e);
                if (o.end !== o.start) {
                    var n = t.ctrlKey || t.metaKey;
                    n && t.keyCode == "K".charCodeAt(0) && habraWYG.insertLink(".tm-editor__toolbar"), n && t.keyCode == "B".charCodeAt(0) && habraWYG.insertTagWithText(".tm-editor__toolbar", "b"), n && t.keyCode == "I".charCodeAt(0) && habraWYG.insertTagWithText(".tm-editor__toolbar", "i")
                }
            }
        }), habraWYG = {
            insertTagWithText: function (e, o) {
                if (t() && "b" === o) return habraWYG.insertTag(e, "**", "**");
                if (t() && "i" === o) return habraWYG.insertTag(e, "*", "*");
                if (t() && "s" === o) return habraWYG.insertTag(e, "~~", "~~");
                if (t() && "h2" === o) return habraWYG.insertTag(e, "## ", "");
                if (t() && "h3" === o) return habraWYG.insertTag(e, "### ", "");
                if (t() && "h4" === o) return habraWYG.insertTag(e, "#### ", "");
                if (t() && "blockquote" === o) return habraWYG.insertTag(e, "> ", "");
                var n = "<" + o + ">", r = "</" + o + ">";
                return habraWYG.insertTag(e, n, r), !1
            }, insertTagWithLatex: function (t, e) {
                var o = e, n = e;
                return habraWYG.insertTag(t, o, n), !1
            }, insertImage: function (e) {
                var o = prompt(window.i18nMsg("TOPIC_FORM_IMAGE_SRC_PROMPT"), "http://");
                if (o) {
                    if (t()) return habraWYG.insertTag(e, "![image", "](" + o + ")");
                    habraWYG.insertTag(e, '<img src="' + o + '" alt="image"/>', "")
                }
                return !1
            }, insertLink: function (e) {
                var o = prompt(window.i18nMsg("TOPIC_FORM_LINK_URL_PROMPT"), "http://");
                if (o) {
                    if (t()) return habraWYG.insertTag(e, "[", "](" + o + ")");
                    habraWYG.insertTag(e, '<a href="' + o + '">', "</a>")
                }
                return !1
            }, insertSpoiler: function (t) {
                return habraWYG.insertTag(t, '<spoiler title="' + window.i18nMsg("TOPIC_FORM_SPOILER_TITLE") + '">\n', "\n</spoiler>"), !1
            }, insertUser: function (t) {
                var e = prompt(window.i18nMsg("TOPIC_FORM_USER_NICKNAME"), "");
                return e && habraWYG.insertTag(t, "@" + e, ""), !1
            }, insertHabracut: function (t) {
                return habraWYG.insertTag(t, "<cut />", ""), !1
            }, insertTag: function (t, e, o, n) {
                var r = $(t).parents('[data-component="tm-editor"]'), s = $("textarea", r).get(0);
                s.focus();
                var a = s.scrollTop, i = habraWYG.getCursor(s), c = s.value.substring(0, i.start),
                    l = s.value.substring(i.start, i.end), u = s.value.substring(i.end);
                if (n && (l = (l = "" != (l = l.replace(/\r/g, "")) ? l : " ").replace(new RegExp(n.findStr, "gm"), n.repStr)), i.start == i.end) var d = i.start + e.length; else d = String(c + e + l + o).length;
                return s.value = c + e + l + o + u, habraWYG.setCursor(s, d, d), a && (s.scrollTop = a), $("textarea", r).trigger("change"), !1
            }, insertTagFromDropBox: function (t) {
                habraWYG.insertTagWithText(t, t.value), t.selectedIndex = 0
            }, insertList: function (e) {
                var o = "<" + e.value + ">\n", n = "\n</" + e.value + ">",
                    r = {findStr: "^(.+)", repStr: "\t<li>$1</li>"};
                t() && "ul" === e.value && (r = {
                    findStr: "^(.+)",
                    repStr: "- $1"
                }, o = "", n = ""), t() && "ol" === e.value && (r = {
                    findStr: "^(.+)",
                    repStr: "1. $1"
                }, o = "", n = ""), habraWYG.insertTag(e, o, n, r), e.selectedIndex = 0
            }, insertSource: function (e) {
                if ("code" === e.value) {
                    var o = "<code>\n", n = "\n</code>";
                    t() ? habraWYG.insertTag(e, "`", "`") : habraWYG.insertTag(e, o, n), e.selectedIndex = 0
                } else {
                    o = '<source lang="' + e.value + '">\n', n = "\n</source>";
                    t() ? habraWYG.insertTag(e, "```" + e.value + "\n", "\n```") : habraWYG.insertTag(e, o, n), e.selectedIndex = 0
                }
            }, insertTab: function (t, e) {
                if (t || (t = window.event), t.keyCode) var o = t.keyCode; else if (t.which) o = t.which;
                switch (t.type) {
                    case"keydown":
                        16 == o && (habraWYG.shift = !0);
                        break;
                    case"keyup":
                        16 == o && (habraWYG.shift = !1)
                }
                e.focus();
                var n = habraWYG.getCursor(e);
                if (n.start == n.end) return !0;
                if (9 == o && !habraWYG.shift) {
                    var r = {findStr: "^(.+)", repStr: "\t$1"};
                    return habraWYG.insertTag(e, "", "", r), !1
                }
                if (9 == o && habraWYG.shift) {
                    r = {findStr: "^\t(.+)", repStr: "$1"};
                    return habraWYG.insertTag(e, "", "", r), !1
                }
            }, getCursor: function (t) {
                var e = {start: 0, end: 0};
                if (t.setSelectionRange) e.start = t.selectionStart, e.end = t.selectionEnd; else {
                    if (!document.selection) return !1;
                    if (document.selection && document.selection.createRange) {
                        var o = document.selection.createRange(), n = o.duplicate();
                        n.moveToElementText(t), n.setEndPoint("EndToEnd", o), e.start = n.text.length - o.text.length, e.end = e.start + o.text.length
                    }
                }
                return e
            }, setCursor: function (t, e, o) {
                if (t.createTextRange) {
                    var n = t.createTextRange();
                    n.move("character", e), n.select()
                } else t.selectionStart && t.setSelectionRange(e, o)
            }, insertLatex: function (t, e) {
                if ("" != t.value) {
                    var o = endTag = e;
                    habraWYG.insertTag(link, o + t.value, endTag)
                }
                return !1
            }
        }
    }(), function () {
        function t() {
            return $("#mdCheck").length ? $("#mdCheck").is(":checked") : !!$("#comment_markdown").length && $("#comment_markdown").is(":checked")
        }

        function e(t) {
            /\bhas-nested/.test(t.parentNode.className) && (t.classList.remove("btn_active"), t.setAttribute("aria-expanded", !1), t.parentNode.classList.remove("dropdown_active"))
        }

        habraWYG2 = {
            insertTagWithText: function (e, o) {
                if (t() && "b" === o) return habraWYG2.insertTag(e, "**", "**");
                if (t() && "i" === o) return habraWYG2.insertTag(e, "*", "*");
                if (t() && "s" === o) return habraWYG2.insertTag(e, "~~", "~~");
                if (t() && "h2" === o) return habraWYG2.insertTag(e, "## ", "");
                if (t() && "h3" === o) return habraWYG2.insertTag(e, "### ", "");
                if (t() && "h4" === o) return habraWYG2.insertTag(e, "#### ", "");
                if (t() && "blockquote" === o) return habraWYG2.insertTag(e, "> ", "");
                var n = "<" + o + ">", r = "</" + o + ">";
                habraWYG2.insertTag(e, n, r)
            }, insertTagWithLatex: function (t, e) {
                var o = e, n = e;
                habraWYG2.insertTag(t, o, n)
            }, insertHstorImage: function (t) {
                if (!window.hstor) return void 0;
                const e = new window.hstor("en_US");
                e.mount(document.getElementById("habrastorage-popup")), e.listen(function (o) {
                    habraWYG2.insertTag(t, o, ""), e.destroy()
                })
            }, insertImage: function (e) {
                var o = prompt(window.i18nMsg("TOPIC_FORM_IMAGE_SRC_PROMPT"), "http://");
                if (o) {
                    if (t()) return habraWYG2.insertTag(e, "![image", "](" + o + ")");
                    habraWYG2.insertTag(e, '<img src="' + o + '" alt="image"/>', "")
                }
                return !1
            }, insertLink: function (e) {
                var o = prompt(window.i18nMsg("TOPIC_FORM_LINK_URL_PROMPT"), "http://");
                if (o) {
                    if (t()) return habraWYG2.insertTag(e, "[", "](" + o + ")");
                    habraWYG2.insertTag(e, '<a href="' + o + '">', "</a>")
                }
                return !1
            }, insertSpoiler: function (t) {
                return habraWYG2.insertTag(t, '<spoiler title="' + window.i18nMsg("TOPIC_FORM_SPOILER_TITLE") + '">\n', "\n</spoiler>"), !1
            }, insertUser: function (t) {
                var e = prompt(window.i18nMsg("TOPIC_FORM_USER_NICKNAME"), "");
                return e && habraWYG2.insertTag(t, "@" + e, ""), !1
            }, insertHabracut: function (t) {
                return habraWYG2.insertTag(t, "<cut />", ""), !1
            }, insertTag: function (t, o, n, r) {
                t = t || null;
                var s = $('[data-component="tm-editor"]'), a = $("textarea", s).get(0);
                t && e(t.parentNode.parentNode.parentNode.getElementsByTagName("button")[0]);
                a.focus();
                var i = a.scrollTop, c = habraWYG2.getCursor(a), l = a.value.substring(0, c.start),
                    u = a.value.substring(c.start, c.end), d = a.value.substring(c.end);
                if (r && (u = (u = "" != (u = u.replace(/\r/g, "")) ? u : " ").replace(new RegExp(r.findStr, "gm"), r.repStr)), c.start == c.end) var _ = c.start + o.length; else _ = String(l + o + u + n).length;
                return a.value = l + o + u + n + d, habraWYG2.setCursor(a, _, _), i && (a.scrollTop = i), !1
            }, insertTagFromDropBox: function (t) {
                habraWYG2.insertTagWithText(t, t.value), t.selectedIndex = 0
            }, insertList: function (e, o) {
                var n = "<" + o + ">\n", r = "\n</" + o + ">", s = {findStr: "^(.+)", repStr: "\t<li>$1</li>"};
                t() && "ul" === o && (s = {
                    findStr: "^(.+)",
                    repStr: "- $1"
                }, n = "", r = ""), t() && "ol" === o && (s = {
                    findStr: "^(.+)",
                    repStr: "1. $1"
                }, n = "", r = ""), habraWYG2.insertTag(e, n, r, s), e.selectedIndex = 0
            }, insertSource: function (o) {
                var n = document.getElementById("list-languages");
                if (document.querySelector(".list-languages_dropdown")) return !1;
                var r = document.createElement("ul");
                r.className = "list-languages list-languages_dropdown";
                var s = document.createElement("li");
                s.className = "list-languages__item", s.appendChild(document.createTextNode("Code")), s.onclick = function (n) {
                    n.preventDefault(), t() ? habraWYG2.insertTag(o, "```", "```") : habraWYG2.insertTag(o, "<code>", "</code>"), e(o)
                }, r.appendChild(s);
                var a = hljs.listLanguages();
                const i = a.indexOf("c-like");
                a.splice(i, 1), a.sort();
                for (var c = 0; c < a.length; c++) {
                    var l = document.createElement("li");
                    l.className = "list-languages__item", l.setAttribute("data-lang", a[c]), l.appendChild(document.createTextNode(a[c])), r.appendChild(l), l.onclick = function (t) {
                        t.preventDefault(), d(t.target.getAttribute("data-lang")), e(o)
                    }
                }
                var u;

                function d(e) {
                    t() ? habraWYG2.insertTag(o, "```" + e + "\n", "\n```") : habraWYG2.insertTag(o, '<source lang="' + e + '">', "</source>")
                }

                return (u = $(r).find('[data-lang="xml"]')).length && $(u).text("XML, HTML"), n.appendChild(r), !1
            }, insertTab: function (t, e) {
                if (t || (t = window.event), t.keyCode) var o = t.keyCode; else if (t.which) o = t.which;
                switch (t.type) {
                    case"keydown":
                        16 == o && (habraWYG2.shift = !0);
                        break;
                    case"keyup":
                        16 == o && (habraWYG2.shift = !1)
                }
                e.focus();
                var n = habraWYG2.getCursor(e);
                if (n.start == n.end) return !0;
                if (9 == o && !habraWYG2.shift) {
                    var r = {findStr: "^(.+)", repStr: "\t$1"};
                    return habraWYG2.insertTag(e, "", "", r), !1
                }
                if (9 == o && habraWYG2.shift) {
                    r = {findStr: "^\t(.+)", repStr: "$1"};
                    return habraWYG2.insertTag(e, "", "", r), !1
                }
            }, getCursor: function (t) {
                var e = {start: 0, end: 0};
                if (t.setSelectionRange) e.start = t.selectionStart, e.end = t.selectionEnd; else {
                    if (!document.selection) return !1;
                    if (document.selection && document.selection.createRange) {
                        var o = document.selection.createRange(), n = o.duplicate();
                        n.moveToElementText(t), n.setEndPoint("EndToEnd", o), e.start = n.text.length - o.text.length, e.end = e.start + o.text.length
                    }
                }
                return e
            }, setCursor: function (t, e, o) {
                if (t.createTextRange) {
                    var n = t.createTextRange();
                    n.move("character", e), n.select()
                } else t.selectionStart && t.setSelectionRange(e, o)
            }
        }
    }()
}, function (t, e) {
    function o(t) {
        t && function (t) {
            return "_blank" === t.getAttribute("target")
        }(t) && function (t, e) {
            var o = t.getAttribute("rel") || "";
            if (!(o.indexOf(e) > -1)) {
                var n = o + " " + e;
                t.setAttribute("rel", n)
            }
        }(t, "noopener")
    }

    t.exports = function () {
        var t, e;
        t = o, ((e = document.querySelectorAll("a")) ? [].slice.apply(e) : []).forEach(function (e) {
            t(e)
        })
    }
}, function (t, e, o) {
    var n = o(2);

    function r(t, e, o) {
        void 0 === o && (o = t - e);
        var n = $(".js-result-popup_title");
        n.html(window.i18nMsg("VOTES_COUNTS_TITLE_ATTR", {votes_count: t, votes_count_plus: e, votes_count_minus: o}));
        var r = $(".js-reasons-vote_result-content"), s = $("#js-result-popup_list"), a = window.voteReasonsList,
            i = window.votesReasonsStat, c = 0;
        if (i.forEach(t => {
            c += t.percent
        }), 0 === o || 0 === c) return $(".js-reasons-vote_result-empty").show(), !1;
        i.forEach(t => {
            var e = $("<div />").addClass("reasons-result__item"), o = $("<div />").text(a[t.id].title),
                n = $("<div />").addClass("reasons-result__percent").text(t.percent + "%"),
                r = $("<div />").addClass("reasons-result__result-line").css({width: t.percent + "%"});
            e.append([o, n, r]), e.appendTo(s)
        }), $("<div />").addClass("reasons-result__top-border").appendTo(s);
        var l = e / t * 100;
        document.documentElement.style.setProperty("--percent-value", l + "%"), n.html(window.i18nMsg("VOTES_COUNTS_TITLE_ATTR", {
            votes_count: t,
            votes_count_plus: e,
            votes_count_minus: o
        })), r.show(), s.show()
    }

    function s() {
        a("js-vote-reason", !1), a("js-vote-result", !1), $(".js-reasons-vote_result-empty").hide(), $("#js-result-popup_list").empty(), $(".js-result-popup_title").empty(), $(".js-reasons-vote_result-content").hide()
    }

    function a(t, e) {
        var o = $("#" + t);
        $("body").toggleClass("overlayed", e), o.toggleClass("hidden", !e)
    }

    function i(t, e) {
        var o = $(t), r = o.closest(".js-post-vote"), s = o.closest(".post-stats"), i = r.find(".js-score"),
            l = o.attr("data-action"), u = $(".js-vote_radio:checked").val(),
            d = {ti: r.attr("data-id"), tt: r.attr("data-type"), v: e, reason: -1 === e ? u : null};
        $.post("/json/vote/", d, function (t) {
            if ("ok" == t.messages) {
                var e = d.v, u = (parseInt(t.score), window.i18nMsg("VOTES_COUNTS_TITLE_ATTR", {
                    votes_count: t.votes_count,
                    votes_count_plus: t.votes_count_plus,
                    votes_count_minus: t.votes_count_minus
                }));
                switch (r.find('button[type="button"]').attr("disabled", "disabled"), e) {
                    case 1:
                        o.attr("title", window.i18nMsg("VOTE_ACTION_PLUS")), o.addClass("voting-wjt__button_plus");
                        break;
                    case-1:
                        o.attr("title", window.i18nMsg("VOTE_ACTION_MINUS")), o.addClass("voting-wjt__button_minus")
                }
                c("voting", s.attr("data-post-type"), l), window.votesReasonsStat = t.reason_map, i.replaceWith('<span onclick="posts_vote_result(' + t.votes_count + ", " + t.votes_count_plus + ')" class="voting-wjt__counter voting-wjt__counter_' + t.sign + ' js-score" title="' + u + '">' + t.score + "</span>"), $(".js-result-popup_title").html(u), a("js-vote-reason", !1), $(".js-vote_send").removeClass("loading")
            } else a("js-vote-reason", !1), n.showSystemError(t)
        }, "json")
    }

    function c(t, e, o) {
        "function" == typeof ga && (ga("send", "event", t, e, o), ga("send", "event", "voting", "publish_ugc_ru", "minus"))
    }

    function l(t) {
        void 0 !== t.subscribe_mark && "1" === t.subscribe_mark && $("#subscribe_comments").length && $("#subscribe_comments").attr("checked", !0), void 0 !== t.tracker_mark && "1" === t.tracker_mark && $("#tracker_comments").length && $("#tracker_comments").attr("checked", !0)
    }

    $(document).on("keyup", t => {
        27 === t.which && s()
    }), $(document).on("click", "#js-vote-reason, #js-vote-result", t => {
        $(t.target).closest(".popup").length || s()
    }), $(document).on("click", ".js-hide_vote-reason, .js-hide_result-reason", s), $(document).on("input, change", ".js-vote_radio", () => {
        $(".js-vote_send").attr("disabled", !1)
    }), $(document).ready(function () {
        $(".post_full .content > :header[id]").each(function () {
            $(this).append('<a class="title_anchor" href="#' + $(this).attr("id") + '"></a>')
        });
        var t = $(".post_full .post__author-controls a.icon-svg_edit");
        t.length && (document.onkeydown = function (e) {
            (e.ctrlKey || e.metaKey) && e.keyCode == "E".charCodeAt(0) && (window.location.href = t.attr("href"))
        }), $(".post_teaser .content > :header[id]").each(function () {
            var t = $(this).closest(".post_teaser").find(".post__title_link").attr("href");
            $(this).append('<a class="title_anchor" href="' + t + "#" + $(this).attr("id") + '"></a>')
        }), $('#edit_tags_form [name="tags_string"]').autocomplete({
            serviceUrl: "/json/suggest/",
            minChars: 2,
            delimiter: /(,|;)\s*/,
            maxHeight: 400,
            width: 300,
            zIndex: 9999,
            deferRequestBy: 500,
            params: {type: "tags"}
        }), $("#lenta_notifications .close").click(function () {
            return $("#lenta_notifications").fadeOut(), $.post("/json/notifications/hint_get_more_blogs/", function (t) {
                "ok" !== t.messages && n.showSystemError(t)
            }, "json"), !1
        }), $(".content-list__item_post.shortcuts_item:not([id])").each(function () {
            0 == this.children.length && this.remove()
        })
    }), void 0 !== t.exports && (t.exports = {
        change_post_subscibptions_checkboxes: l, closeForm: function (t, e) {
            $(t).closest("form").toggleClass("hidden", e)
        }, posts_add_to_favorite: function (t) {
            var e = $(t), o = e.find(".js-favs_count"), r = o.html() ? parseInt(o.html()) : 0,
                s = {tt: e.attr("data-type"), ti: e.attr("data-id"), action: e.attr("data-action")};
            return $.post("/json/favorites/", s, function (t) {
                "ok" == t.messages ? (e.toggleClass("bookmark-btn_is-added", "add" == s.action), "add" == s.action ? ($.jGrowl(window.i18nMsg("POST_ACTION_FAVORITES_ADDED"), {}), e.attr({
                    "data-action": "remove",
                    title: window.i18nMsg("FAVORITES_REMOVE_TITLE_ATTR")
                }), o.html(r += 1), $(".js-fav-edit-link").removeClass("hidden"), l(t), c("favorites", e.attr("data-post-type"), window.g_user_login)) : ($.jGrowl(window.i18nMsg("POST_ACTION_FAVORITES_REMOVED"), {}), e.attr({
                    "data-action": "add",
                    title: window.i18nMsg("FAVORITES_ADD_TITLE_ATTR")
                }), o.html(r -= 1), $(".js-fav-edit-link").addClass("hidden")), e.blur()) : n.showSystemError(t)
            }, "json"), !1
        }, posts_draft_delete: function (t) {
            var e = $(t);
            if (confirm(window.i18nMsg("DRAFT_REMOVE_ACTION_ALERT"))) {
                var o = e.attr("data-draft-delete"), r = e.attr("data-post-id");
                return $.post(o, {id: r}, function (t) {
                    "ok" === t.messages ? void 0 !== t.redirect ? document.location.href = t.redirect : $.jGrowl('SYSTEM redirect to: <a href="' + t.redirect_debug + '">' + t.redirect_debug + "</a>", {
                        theme: "error",
                        sticky: !0
                    }) : n.showSystemError(t)
                }), !1
            }
        }, posts_vote_minus: function (t) {
            if (!window.voteReasonsList) return !1;
            var e, o, n, r, s;
            a("js-vote-reason", !0), 0 === $("#js-vote-popup_list").children().length && (Object.keys(window.voteReasonsList).map(t => window.voteReasonsList[t]).sort((t, e) => t.order < e.order ? -1 : t.order > e.order ? 1 : 0).forEach(t => (t = t, o = $("#js-vote-popup_list"), n = $(".js-vote-popup_list-item").clone(), r = n.find(".js-vote_radio"), s = n.find(".js-vote_title"), r.val(t.id), s.text(t.title), r.attr("id", r.attr("id") + t.id), s.attr("for", s.attr("for") + t.id), n.removeClass("js-vote-popup_list-item").appendTo(o), void o.show())), $(document).on("click", ".js-vote_send", () => {
                $(".js-vote_send").addClass("loading").attr("disabled", !0), i(t, -1)
            }))
        }, posts_vote_plus: function (t) {
            i(t, 1)
        }, posts_vote_result: function (t, e, o) {
            if (!window.votesReasonsStat) return !1;
            a("js-vote-result", !0), r(t, e, o)
        }, posts_list_vote_result: function (t, e, o, s) {
            var i = $("<div />").addClass("overlay").appendTo(document.body);
            $.ajax({
                url: "/json/vote/post_vote_reasons/?post_id=" + s, type: "POST", success: function (s) {
                    if (i.remove(), s.system_errors) return n.showSystemError(s), !1;
                    window.votesReasonsStat = s.data, a("js-vote-result", !0), r(t, e, o)
                }
            })
        }, postsStatAddCountForAllComments: function (t) {
            const e = $("#post-stats-comments-count"), o = parseInt(e.text(), 10), n = isNaN(o) ? 0 : o;
            e.text(n + t)
        }, showAbuseForm: function () {
            var t = $("#abuse_form"), e = t.find('input[name="text"]'), o = t.find('button[type="submit"]');
            e.on("change keyup", function () {
                var t = "" == $(this).val();
                o.attr("disabled", t)
            }), t.ajaxForm({
                form: t, beforeSubmit: function (r, s, a) {
                    n.ajaxFormBeforSubmit(r, s, a);
                    var i = e.val();
                    if (i = i.replace(/\s+/g, ""), o.attr("disabled", !0), t.toggleClass("hidden", !0), "" === i) return !1
                }, success: n.ajaxFormSuccess(function (e, n, r, s) {
                    $.jGrowl(window.i18nMsg("ABUSE_SUBMITTED"), {}), t.resetForm(), o.attr("disabled", !0), t.toggleClass("hidden", !0)
                }, !1, !0)
            }), t.toggleClass("hidden")
        }, show_edit_tags: function (t) {
            var e = $(t), o = $("#edit_tags_form"), r = [], s = {type: e.attr("data-type"), id: e.attr("data-id")},
                a = $("#post_" + s.id).find(".fav");
            a.each(function () {
                r[r.length] = $(this).text()
            }), o.find('button[type="submit"]').attr("disabled", !1), o.find('input[name="tt"]').val(s.type), o.find('input[name="ti"]').val(s.id), o.find('input[name="tags_string"]').val(r.join(", ")).focus(), o.toggleClass("hidden"), o.size() && o.ajaxForm({
                form: o,
                beforeSubmit: n.ajaxFormBeforSubmit,
                success: n.ajaxFormSuccess(function (t, e, n, r) {
                    var s = o.find('input[name="ti"]').val();
                    for (k in o.find('input[name="tt"]').val(), a.parent().remove(), t.user_tags) $("#post_" + s + " .js-post-tags").append('<li class="inline-list__item inline-list__item_tag"><a href="/tag/' + t.user_tags[k] + '/" rel="tag" class="inline-list__item-link post__tag fav js-fav-tag">' + t.user_tags[k] + "</a></li>");
                    o.toggleClass("hidden", !0)
                })
            })
        }, show_recommend_form: function () {
            var t = $("#admin_causes");
            !function (t) {
                t.removeClass("hidden")
            }(t), $(".form__input_checkbox", t).on("change", function () {
                var t, e, o, n;
                o = $(".form__input_checkbox", t).toArray().some(function (t) {
                    return $(t).prop("checked")
                }), n = $('button[type="submit"]', t), e = !o, n.prop("disabled", e)
            })
        }
    })
}, function (t, e, o) {
    var n = o(27);
    void 0 !== t.exports && (t.exports = {
        addCountForAllComments: function (t) {
            var e = $("#comments_count"), o = parseInt(e.text(), 10);
            e.text(o + t)
        }, appendNewComments: function (t) {
            t.forEach(function (t) {
                !function (t) {
                    (function (t) {
                        return $("#comment_" + t.id).length > 0
                    })(t) || (function (t) {
                        if (function (t) {
                            return "0" === t.parent_id || "0" === t.parrent_id
                        }(t)) return $("#comments-list");
                        var e = t.parent_id || t.parrent_id;
                        return $("#reply_comments_" + e)
                    }(t).append(t.html), n.showCommentParents(t.id), $(document).trigger("hljsUpdate"))
                }(t)
            })
        }
    })
}, , , , , , , , , , , , function (t, e, o) {
    var n = o(2), r = o(22);

    function s(t) {
        $("body").toggleClass("overlayed", t), $("#js-lang_settings").toggleClass("hidden", !t)
    }

    $(document).ready(function () {
        var t, e, o;
        t = $("#fl_langs_ru"), e = $("#fl_langs_en"), o = $(".js-popup_save_btn"), $.cookie("fl") || ($("#hl_langs_ru").on("change", function () {
            t.prop("checked", !0), e.prop("checked", !1), o.attr("disabled", !1)
        }), $("#hl_langs_en").on("change", function () {
            t.prop("checked", !1), e.prop("checked", !0), o.attr("disabled", !1)
        })), $(".js-show_lang_settings").on("click", function (t) {
            t.preventDefault(), $(".dropdown_user").removeClass("dropdown_active"), s(!0)
        }), $(".js-hide_lang_settings").on("click", function (t) {
            t.preventDefault(), s(!1)
        }), $(document).on("keyup", function (t) {
            27 === t.which && s(!1)
        }), $("#js-lang_settings").on("click", function (t) {
            $(t.target).closest(".popup").length || s(!1)
        }), $(".js-hl_langs").on("change", function () {
            !function (t) {
                var e = r[t], o = {
                    fl_legend: e.SETTINGS_LANGUAGE_PUBLICATIONS,
                    hl_legend: e.SETTINGS_LANGUAGE_INTERFACE,
                    save_btn: e.SAVE_SETTINGS,
                    title: e.SETTINGS_LANGUAGE,
                    feed_ru: e.SETTINGS_LANGUAGE_FEED_RU,
                    feed_en: e.SETTINGS_LANGUAGE_FEED_EN
                };
                $.each(o, function (t) {
                    $(".js-popup_" + t).html(o[t])
                })
            }($(this).val())
        }), $(".js-fl_langs").on("change", function () {
            var t, e;
            t = $(".js-fl_langs:checked"), e = $(".js-hl_langs:checked").val(), t.length ? $("div.jGrowl-close").triggerHandler("click") : $.jGrowl(r[e].SETTINGS_LANGUAGE_ERROR_MESSAGE, {
                sticky: !0,
                theme: "error",
                closeTemplate: ""
            }), $(".js-popup_save_btn").attr("disabled", !t.length)
        }), $("#lang-settings-form").ajaxForm({
            form: $("#lang-settings-form"),
            error: n.ajaxFormError,
            success: n.ajaxFormSuccess(function (t) {
                "ok" == t.messages && ($.jGrowl(window.i18nMsg("LANGUAGE_FORM_SUCCESS_MESSAGE")), s(!1), window.location.reload())
            })
        })
    })
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        var t = $("article.post").find("form.poll"), e = $("input.js-field-data", t);
        e.on("change", function () {
            var t = $(this).closest("form.poll"), o = e.is(":checked");
            $('button[data-action="poll"]', t).prop("disabled", !o)
        })
    }), void 0 !== t.exports && (t.exports = {
        vote: function (t) {
            var e = $(t), o = e.closest("form.poll"), r = e.closest(".js-post-polling"),
                s = {ti: e.attr("data-id"), action: e.attr("data-action")};
            if (!o.length) throw new Error("Form for poll element doesn’t exist!");
            o.ajaxSubmit({
                data: s,
                beforeSubmit: n.ajaxFormBeforSubmit,
                success: n.ajaxFormSuccess(function (t, e, o, n) {
                    r.replaceWith(t.question_html)
                })
            })
        }
    })
}, function (t, e, o) {
    o(62), o(63), o(64), o(65), o(67), o(68), o(69);
    var n = o(70), r = o(7), s = o(27), a = o(23), i = o(71), c = o(72), l = o(73), u = o(74), d = o(75), _ = o(29),
        m = o(30), f = o(36), p = o(76), h = o(77), g = o(35);
    window.comment_delete = c.comment_delete, window.comment_preview = i.comment_preview, window.comments_add_to_favorite = n.comments_add_to_favorite, window.comment_send = l.comment_send, window.comment_show_edit_form = u.comment_show_edit_form, window.comment_show_form = d.comment_show_form, window.comment_show_reply_form = _.comment_show_reply_form, window.comment_update = p.comment_update, window.comment_vote = h.comment_vote, window.instagrammProcess = a.instagrammProcess, window.showCommentParents = s.showCommentParents;
    m.endTimer, f.listenCommentFormTitle, r.setCommentForm, r.showCommentButtons, m.startTimer;
    var E = window.mention_autocomplete, T = (window.empty, !1);

    function v(t) {
        for (var e = window.location.search.substring(1).split("&"), o = 0; o < e.length; o++) {
            var n = e[o].split("=");
            if (n[0] == t) return unescape(n[1])
        }
        return !1
    }

    (function (t) {
        try {
            var e = window[t], o = "__storage_test__";
            return e.setItem(o, o), e.removeItem(o), !0
        } catch (t) {
            return !1
        }
    })("localStorage") && (T = !0), document.unreadcomments = [], $(function () {
        E($("#comment_text"))
    }), $(document).ready(function () {
        if ($(".comment_item .bad").on("touchstart", function () {
            $(this).toggleClass("focused")
        }), $(document).on("click", ".comment_item .html_format a", function () {
            $(this).attr("target", "_blank")
        }), v("reply_to")) {
            var t = v("reply_to");
            comment_show_reply_form(t)
        }
        if (g_show_xpanel && g.initXPanel(), $(document).on("click", ".comment__collapse", function () {
            S($(this).attr("data-id"))
        }), T) {
            var e = C();
            e.length && e.forEach(t => {
                S(t)
            })
        }
        var o, n = (o = window.location.hash) && o.length && -1 !== o.indexOf("#comment_") ? $(o) : null;
        n && (n.is(":hidden") || n.hasClass("comment_collapsed")) && function t(e) {
            var o = $(e).parents(".js-comment_collapsed:last");
            if (o.length) S(o.attr("rel")), t(e); else {
                var n = -10 + parseInt($(e).css("padding-top"), 10) || -10;
                $.scrollTo(e, 100, {axis: "y", offset: n})
            }
        }(n);
        var r = $(".main-navbar").height();
        $(document).on("click", ".icon_comment-anchor", function (t) {
            var e = $(this).attr("href");
            "#" === e[0] && (t.preventDefault(), $.scrollTo(e, 400, {
                axis: "y",
                offset: -1 * r - 4
            }), window.history.pushState({}, "", e))
        })
    });
    var b = 500;

    function S(t) {
        var e = $("#comment_" + t), o = $('.comment__collapse[data-id="' + t + '"]'),
            n = $("#comment_" + t + " + .content-list .content-list__item").length + 1, r = $("#reply_comments_" + t);
        e.find("> div").toggle(), e.toggleClass("comment_collapsed"), e.closest(".js-comment").toggleClass("js-comment_collapsed"), o.is(":empty") ? o.text(window.i18nMsg("TOGGLE_BRANCH") + " (" + n + ")") : o.empty(), r.toggle(), T && (r.is(":visible") ? function (t) {
            var e = window.articleId, o = w(), n = O(), r = C(), s = r.indexOf(t);
            -1 !== s && r.splice(s, 1);
            if (0 === r.length) {
                var a = o.indexOf(e);
                -1 !== a && o.splice(a, 1), localStorage.setItem("habr-articles-stack", JSON.stringify(o)), delete n[e]
            } else Object.assign(n, {[e]: r});
            localStorage.setItem("habr-collapsed-comments-hash", JSON.stringify(n))
        }(t) : function (t) {
            var e = window.articleId, o = w(), n = O(), r = C();
            -1 === r.indexOf(t) && r.push(t);
            n[e] || o.push(e);
            if (Object.assign(n, {[e]: r}), o.length > b) {
                var s = o.shift();
                delete n[s]
            }
            localStorage.setItem("habr-collapsed-comments-hash", JSON.stringify(n)), localStorage.setItem("habr-articles-stack", JSON.stringify(o))
        }(t))
    }

    function w() {
        return JSON.parse(localStorage.getItem("habr-articles-stack") || "[]")
    }

    function O() {
        return JSON.parse(localStorage.getItem("habr-collapsed-comments-hash") || "{}")
    }

    function C() {
        var t = window.articleId, e = O();
        return e && e[t] ? e[t] : []
    }
}, function (t, e) {
    $(document).ready(function () {
        $(".abuse_link").on("click", function () {
            var t = $(this), e = window.confirm(window.i18nMsg("COMMENT_ABUSE_CONFIRM")),
                o = {tt: $(this).data("tt"), ti: $(this).data("ti")};
            return e && $.post($(this).data("url"), o, function (e) {
                "ok" == e.messages ? t.replaceWith('<span class="comment__footer-notice comment__footer-notice_sucess">' + window.i18nMsg("COMMENT_ABUSE_NOTICE") + "</span>") : show_form_errors(e)
            }), !1
        })
    })
}, function (t, e) {
    $(document).ready(function () {
        $("#comment_markdown").on("change", function (t) {
            var e = $(t.currentTarget);
            !function (t) {
                $.post("/json/comment/markdown/", {action: "markdown", state: t})
            }(Boolean(e.prop("checked")))
        })
    })
}, function (t, e) {
    $(document).ready(function () {
        var t = {SUBMIT: ".js-h-message__submit", MESSAGE: ".js-h-message"},
            e = {BTN_LOADING: "loading", FORM_HIDDEN: "hidden"};

        function o(o, n) {
            var r = $(t.SUBMIT, o);
            r.toggleClass(e.BTN_LOADING, n), r.prop("disabled", n)
        }

        function n(t, o) {
            if (o) t.addClass(e.FORM_HIDDEN); else {
                t.fadeOut(300, function () {
                    t.addClass(e.FORM_HIDDEN)
                })
            }
        }

        $(document).on("click", t.SUBMIT, function (r) {
            r.preventDefault();
            var s = $(r.currentTarget);
            if (!s.hasClass(e.BTN_LOADING)) {
                var a = s.closest(t.MESSAGE), i = function (t) {
                    if (!t.length) return null;
                    var e = t.serializeArray();
                    return e.length ? e.reduce(function (t, e) {
                        return t[e.name] = e.value, t
                    }, {}) : null
                }(a);
                if (!i) return n(a, !0), void $("#comments_form_placeholder").show();
                !function (t, e) {
                    o(t, !0);
                    var r = e["user-id"], s = e.url;
                    $.post(s, {user_id: r}, function () {
                        o(t, !1), n(t)
                    })
                }(a, i)
            }
        })
    })
}, function (t, e, o) {
    var n = o(27), r = o(23), s = o(35), a = window.change_post_subscibptions_checkboxes,
        i = (window.hljs, r.instagrammProcess), c = n.showCommentParents, l = window.show_system_error;
    $(document).on("click", ".comment-moderate", function (t) {
        var e = $(t.currentTarget), o = e.data("id"), n = e.data("action");
        return e.addClass("loading"), $.post("/json/comment/", {id: o, action: "moderate", status: n}, function (t) {
            if ("approve" == n) if ("ok" == t.messages) {
                var r;
                for (r in t.comments) 0 == $("#comment_" + t.comments[r].id).size() && (0 == t.comments[r].parent_id ? $("#comment_" + o).replaceWith(t.comments[r].html) : $("#reply_comments_" + t.comments[r].parent_id).find("#comment_" + o).replaceWith(t.comments[r].html), c(t.comments[r].id), document.unreadcomments.push({id: parseInt(t.comments[r].id)}));
                s.syncCommentCountFromDOM(), $("#comments_count").text(parseInt($("#comments_count").text()) + t.comments.length), $(document).trigger("hljsUpdate"), a(t), i()
            } else l(t); else "ok" == t.messages ? $("#comment_" + o).remove() : l(t);
            e.removeClass("loading")
        }, "json"), !1
    })
}, function (t, e, o) {
    void 0 !== t.exports && (t.exports = {
        COUNTER: function (t) {
            return '<span class="new" title="' + window.i18nMsg("XPANEL_COUNTER_TITLE_ATTR") + '">' + t + "</span>"
        }, NEXT_BUTTON: function () {
            return '<span class="next_new" title="' + window.i18nMsg("XPANEL_NEXT_BUTTON_TITLE_ATTR") + '">&darr;</span>'
        }, PREV_BUTTON: function () {
            return '<span class="prev_new" title="' + window.i18nMsg("XPANEL_PREV_BUTTON_TITLE_ATTR") + '">&uarr;</span>'
        }, REFRESH_BUTTON: function () {
            return '<span class="refresh" title="' + window.i18nMsg("XPANEL_REFRESH_BUTTON_TITLE_ATTR") + '"></span>'
        }, WRAPPER: function () {
            return '<div id="xpanel"></div>'
        }
    })
}, function (t, e) {
    $(document).ready(function () {
        $("*[rel=user-popover]").each(function () {
            var t = $(this);
            t.webuiPopover({
                animation: "fade",
                arrow: !1,
                padding: !1,
                placement: "bottom-right",
                trigger: "hover",
                offsetTop: 5,
                delay: {show: 1e3},
                type: "async",
                url: "/json/users/info/",
                async: {method: "POST", data: {login: t.data("user-login")}},
                content: function (t) {
                    return t.html
                }
            })
        })
    })
}, function (module, exports) {
    $(document).ready(function () {
        var SELECTORS = {
            BUTTON: "#hide_all_deleted_comments",
            COMMENTS: ".js-comment_deleted",
            SUBSCRIBE_BY_EMAIL: "#subscribe_comments",
            SUBSCRIBE_BY_TRACKER: "#tracker_comments"
        };

        function toggleHideDeletedBtnState(t, e) {
            var o = e ? window.i18nMsg("COMMENTS_REMOVED_SHOW") : window.i18nMsg("COMMENTS_REMOVED_HIDE");
            t.text(o), t.data("deleted-comments-hidden", e)
        }

        function toggleDeletedCommentsState(t) {
            var e = $(SELECTORS.COMMENTS);
            t ? e.hide() : e.show()
        }

        function onHideDeletedClick(t) {
            t.preventDefault();
            var e = $(t.currentTarget), o = !e.data("deleted-comments-hidden");
            toggleHideDeletedBtnState(e, o), toggleDeletedCommentsState(o)
        }

        function onSubscribeByEmailClick() {
            var subscribe_checkbox = $(this);
            eval("var data = " + subscribe_checkbox.attr("rel") + ";"), data.action = subscribe_checkbox.attr("checked") ? "subscribe" : "unsubscribe", $.post("/json/subscription/", data, function (t) {
                "ok" == t.messages ? $.jGrowl(window.i18nMsg("COMMENTS_SETTINGS_SAVED_NOTICE"), {theme: "notice"}) : show_system_error(t)
            }, "json")
        }

        function onSubscribeByTrackerClick() {
            var checkbox = $(this);
            eval("var data = " + checkbox.attr("rel") + ";");
            var url = checkbox.prop("checked") ? "/json/tracker/add/" : "/json/tracker/remove/";
            $.post(url, data, function (t) {
                "ok" == t.messages ? $.jGrowl(window.i18nMsg("COMMENTS_SETTINGS_SAVED_NOTICE"), {theme: "notice"}) : show_system_error(t)
            }, "json")
        }

        $(SELECTORS.BUTTON).on("click", onHideDeletedClick), g_is_guest && ($(SELECTORS.SUBSCRIBE_BY_EMAIL).on("change", onSubscribeByEmailClick), $(SELECTORS.SUBSCRIBE_BY_TRACKER).on("change", onSubscribeByTrackerClick))
    })
}, function (t, e) {
    $(document).ready(function () {
        var t = $(".main-navbar").height();
        $(document).on("click", ".js-comment_parent", function () {
            var e = $(this).data("parent_id"), o = $(this).data("id");
            return $(this).hasClass("back_to_children") ? $("#comment_" + o).find(".js-comment_chidren").addClass("hidden") : ($("#comment_" + e).find(".js-comment_children").removeClass("hidden"), $("#comment_" + e).find(".js-comment_children > .js-comment_parent").attr({
                href: "#comment_" + o,
                "data-id": e,
                "data-parent_id": o
            })), $.scrollTo($("#comment_" + e), 400, {axis: "y", offset: -1 * t - 4}), !1
        }), $(document).on("click", ".js-comment_tree", function (e) {
            var o = $(e.currentTarget), n = $(".js-comment"), r = !o.hasClass("open");
            n.toggle(!r), o.toggleClass("open", r);
            var s, a, i = o.data("id"), c = $("#comment_" + i);
            r && showCommentParents(i), s = c, a = $(window).height(), $.scrollTo(s, 1, {
                axis: "y",
                offset: {top: a / 2 * -1 - -1 * t}
            })
        })
    })
}, function (t, e, o) {
    var n = window.show_system_error, r = {BOOKMARKED: "icon_comment-bookmark_add"};

    function s(t, e) {
        var o = function (t) {
            return "add" === t ? window.i18nMsg("COMMENT_FAVORITE_ADDED") : window.i18nMsg("COMMENT_FAVORITE_REMOVED")
        }(e), n = function (t) {
            return "add" === t ? "remove" : "add"
        }(e);
        $.jGrowl(o, {}), t.data("action", n)
    }

    void 0 !== t.exports && (t.exports = {
        comments_add_to_favorite: function (t) {
            var e = $(t), o = e.data("action"), a = {tt: e.data("type"), ti: e.data("id"), action: o};
            return e.toggleClass(r.BOOKMARKED, "add" === a.action), $.post("/json/favorites/", a, function (t) {
                (function (t) {
                    return "ok" === t.messages || (n(t), !1)
                })(t) && s(e, o)
            }, "json"), !1
        }
    })
}, function (t, e, o) {
    var n = o(7), r = window.ajaxFormSuccess;

    function s(t, e) {
        var o;
        return '<div class="comment__head">' + (o = window.g_user_login, '<span class="user_info user-info_inline">  <img src=' + window.g_user_avatar + ' class="user-info__image-pic user-info__image-pic_small" />  <span class="user-info__nickname user-info__nickname_small user-info__nickname_comment">' + o + "   </span></span>") + ('<time class="comment__date-time">' + e + "</time>") + "</div>" + t
    }

    function a(t, e) {
        t.toggleClass("loading", e)
    }

    void 0 !== t.exports && (t.exports = {
        comment_preview: function (t, e) {
            var o = $(e);
            $(t).ajaxSubmit({
                data: {action: "preview"}, beforeSubmit: function (t, e) {
                    return a(o, !0), !!n.validateFormInput(e) || (a(o, !1), !1)
                }, success: r(function (t) {
                    var e = s(t.text, t.time_published);
                    $("#preview_placeholder").removeClass("hidden").html(e), a(o, !1), $(document).trigger("hljsUpdate")
                })
            })
        }
    })
}, function (t, e, o) {
    var n = o(28), r = o(29), s = window.ajaxFormError, a = window.ajaxFormSuccess,
        i = {COMMENT: "#comment_{id}", FOOTER: ".comment__footer", HEADER: ".comment__head", VOTE: ".js-comment-vote"},
        c = {COMMENT_DELETED: "js-comment_deleted", HEADER_DELETED: "comment__head_deleted"},
        l = {HIDDEN: '<span class="comment__status">' + window.i18nMsg("COMMENT_HIDDEN") + "</span>"};

    function u(t) {
        r.comment_show_reply_form(null, !0), function (t) {
            t.addClass(c.COMMENT_DELETED);
            var e = $(i.HEADER, t);
            e.addClass(c.HEADER_DELETED), e.append(l.HIDDEN)
        }($(i.COMMENT.replace("{id}", t.comment_id)))
    }

    void 0 !== t.exports && (t.exports = {
        comment_delete: function (t, e) {
            confirm(window.i18nMsg("COMMENT_REMOVE_CONFIRM")) ? (n.toggleLoadingState($(t), $(e), !0), function (t) {
                t.ajaxSubmit({data: {action: "delete"}, form: t, dataType: "json", error: s, success: a(u, null, !0)})
            }($(t))) : n.hideForm(t)
        }
    })
}, function (t, e, o) {
    var n = o(47), r = o(28), s = o(7), a = o(27), i = o(23), c = o(35), l = o(46), u = window.ajaxFormError,
        d = window.ajaxFormSuccess, _ = window.change_post_subscibptions_checkboxes,
        m = (window.empty, window.hljs, i.instagrammProcess);
    a.showCommentParents;
    void 0 !== t.exports && (t.exports = {
        comment_send: function (t, e, o) {
            o.preventDefault();
            var a = $(t), i = $(e);
            a.ajaxSubmit({
                data: {action: "add"}, dataType: "json", beforeSubmit: function () {
                    return r.toggleLoadingState(a, i, !0), !!s.validateFormInput(a) || (r.toggleLoadingState(a, i, !1), !1)
                }, error: function (t, e) {
                    r.toggleLoadingState(a, i, !0), u(t, e)
                }, success: d(function (t, e, o, s) {
                    var u, d, f;
                    u = "comment", d = a.attr("data-post-type"), f = window.g_user_login, "function" == typeof ga && ga("send", "event", u, d, f), r.toggleLoadingState(a, i, !1);
                    var p = t.comments_moderation;
                    p.html && (p = [p]);
                    var h, g = [].concat.apply(t.comments || [], p || []);
                    n.appendNewComments(g), t.comments_moderation && ($("#moderated_placeholder").removeClass("hidden"), r.showForm(), $("#comments_form_placeholder").hide()), t.ts && r.updateTimestamp(a, t.ts), c.syncCommentCountFromDOM(), r.clearInputField(a), r.hidePreview(a), h = a, r.isRootCommentsForm(h) || r.hideForm(a), n.addCountForAllComments(g.length), l.postsStatAddCountForAllComments(g.length), $(document).trigger("hljsUpdate"), m(), _(t);
                    var E = document.querySelector('div[rel="' + t.new_comment_id + '"] .user-comment__container');
                    t.ufo && "kidnapping" === t.ufo ? setTimeout(() => {
                        T(E)
                    }) : t.ufo && "kidnapped" === t.ufo && setTimeout(() => {
                        var t = document.createElement("img");
                        E.querySelector(".user-info__image-pic:first-child").style.display = "none", t.src = "https://gravatar.com/avatar/" + md5(window.g_user_login.toLowerCase()) + "?s=48&d=monsterid&r=x", t.classList.add("user-info__image-pic_small"), t.classList.add("user-info__image-pic"), document.getElementById("body"), E.appendChild(t)
                    })
                }, function () {
                    r.toggleLoadingState(a, i, !1)
                })
            })
        }
    });
    const f = 5, p = 400, h = -400, g = 10, E = 40, T = t => {
        html2canvas(t, {allowTaint: !1, useCORS: !1, backgroundColor: "#7aa1bd", scale: 1}).then(e => {
            const o = E, n = e.getContext("2d").getImageData(0, 0, e.width, e.height), r = n.data,
                s = n.data.slice(0).fill(0), a = Array.from({length: o}, t => s.slice(0));
            for (let t = 0; t < r.length; t += 4) {
                const e = Math.floor(t / r.length * o), n = a[v(e, o)];
                n[t] = r[t], n[t + 1] = r[t + 1], n[t + 2] = r[t + 2], n[t + 3] = r[t + 3]
            }
            for (let n = 0; n < o; n++) {
                const o = S(a[n], e.width, e.height);
                o.classList.add("dust");
                const r = 1e3 * f;
                setTimeout(() => {
                    b(o, p, h, chance.integer({min: -g, max: g}), r), o.classList.add("blur"), setTimeout(() => {
                        o.remove()
                    }, r + 50)
                }, 65 * n + 4 * f), t.appendChild(o)
            }
            Array.from(t.querySelectorAll(":not(.dust)")).map(t => {
                t.classList.add("quickFade")
            }), setTimeout(() => {
                var e = document.createElement("img");
                t.querySelector(".user-info__image-pic:first-child").style.display = "none", e.src = "https://gravatar.com/avatar/" + md5(window.g_user_login) + "?s=48&d=monsterid&r=x", e.classList.add("user-info__image-pic_small"), e.classList.add("user-info__image-pic"), document.getElementById("body"), t.appendChild(e)
            }, 5e3)
        }).catch(function (t) {
        })
    }, v = (t, e) => {
        const o = [], n = [];
        for (let r = 0; r < e; r++) o.push(Math.pow(e - Math.abs(t - r), E / 2)), n.push(r);
        return chance.weighted(n, o)
    }, b = (t, e, o, n, r) => {
        tx = ty = 0;
        t.animate([{transform: "rotate(0) translate(0, 0)"}, {transform: "rotate(" + n + "deg) translate(" + e + "px," + o + "px)"}], {
            duration: r,
            easing: "ease-in"
        })
    }, S = (t, e, o) => {
        const n = document.createElement("canvas");
        return n.width = e, n.height = o, tempCtx = n.getContext("2d"), tempCtx.putImageData(new ImageData(t, e, o), 0, 0), n
    }
}, function (t, e, o) {
    var n = o(28), r = o(7), s = o(29), a = o(30), i = o(36), c = a.endTimer, l = i.listenCommentFormTitle,
        u = r.setCommentForm, d = r.showCommentButtons, _ = a.startTimer, m = s.comment_show_reply_form,
        f = window.mention_autocomplete, p = window.show_system_error;
    void 0 !== t.exports && (t.exports = {
        comment_show_edit_form: function (t) {
            c();
            var e = $("#comment_" + t), o = $("#comment-form"), r = o.find(".time_left");
            return c(), n.hidePreview(o), $.post("/json/comment/", {comment_id: t, action: "source"}, function (n) {
                if ("ok" == n.messages) {
                    var s = n.text;
                    o.find(".js-form-buttons"), e.find(".js-form_placeholder").append(o), e.children(".js-form_placeholder").removeClass("hidden"), u(o, 0, t, s), f($("#comment_text")), d("edit"), _(r, n.time_left, 1e3), l(Boolean(t), m)
                } else p(n)
            }), !1
        }
    })
}, function (t, e, o) {
    var n = o(7), r = o(30).endTimer, s = n.setCommentForm, a = n.showCommentButtons, i = window.mention_autocomplete;
    void 0 !== t.exports && (t.exports = {
        comment_show_form: function (t) {
            r();
            var e = $("#comment_" + t), o = $("#comment-form");
            return e.children(".js-form_placeholder").removeClass("hidden"), e.children("#comment-form_placeholder").html(o), s(o, t, 0, ""), o.find(".time_left").text(""), i($("#comment_text")), a("reply"), !1
        }
    })
}, function (t, e, o) {
    var n = o(23), r = o(29).comment_show_reply_form, s = window.ajaxFormError, a = window.ajaxFormSuccess,
        i = window.empty, c = (window.hljs, n.instagrammProcess);
    void 0 !== t.exports && (t.exports = {
        comment_update: function (t, e) {
            $(t).ajaxSubmit({
                data: {action: "edit"}, form: $(t), dataType: "json", beforeSubmit: function () {
                    $(e).addClass("loading");
                    var o = $('textarea[name="text"]', t).val();
                    if (i(o)) return $.jGrowl(window.i18nMsg("COMMENT_NOTICE_TEXT_REQIRED"), {theme: "error"}), $(e).removeClass("loading"), !1
                }, error: function (t, o) {
                    $(e).removeClass("loading"), s(t, o)
                }, success: a(function (t, o, n, s) {
                    $("#preview_placeholder").addClass("hidden"), $(e).removeClass("loading"), r(null, !0);
                    var a = $("#comment_" + t.comment_id);
                    $("> .comment__message", a).html(t.message), $("> .comment__date-time", a).html(t.time_changed + ' <span class="comment-item__time_changed icon_comment-edit" title="' + window.i18nMsg("COMMENT_WAS_EDITED_TITLE") + '"><i class="comment-item__sign_changed">(' + window.i18nMsg("COMMENT_WAS_EDITED_TITLE") + ")</i></span>"), $(document).trigger("hljsUpdate"), c()
                }, null, !0)
            })
        }
    })
}, function (t, e, o) {
    void 0 !== t.exports && (t.exports = {
        comment_vote: function (t, e) {
            var o = $(t), n = o.closest(".js-comment-vote"), r = n.find(".js-score"), s = e || "/json/vote/",
                a = o.attr("data-action"), i = {
                    ti: n.attr("data-id"), tt: n.attr("data-type"), pti: n.attr("data-post-target"), v: function (t) {
                        return "plus" == t ? parseInt(1) : parseInt(-1)
                    }(a)
                };
            $.post(s, i, function (t) {
                if ("ok" == t.messages) {
                    var e = i.v, s = (parseInt(t.score, 10), window.i18nMsg("VOTES_COUNTS_TITLE_ATTR", {
                        votes_count: t.votes_count,
                        votes_count_plus: t.votes_count_plus,
                        votes_count_minus: t.votes_count_minus
                    }));
                    switch (n.find('button[type="button"]').each(function () {
                        $(this).attr("disabled", "disabled"), $(this).blur()
                    }), e) {
                        case 1:
                            o.attr("title", window.i18nMsg("VOTE_ACTION_PLUS")), o.addClass("voting-wjt__button_plus");
                            break;
                        case-1:
                            o.attr("title", window.i18nMsg("VOTE_ACTION_MINUS")), o.addClass("voting-wjt__button_minus")
                    }
                    r.replaceWith('<span class="voting-wjt__counter voting-wjt__counter_' + t.sign + ' js-score" title="' + s + '">' + t.score + "</span>"), function (t, e, o) {
                        "function" == typeof ga && ga("send", "event", t, e, o)
                    }("comment", a, window.g_user_login)
                } else show_system_error(t)
            }, "json")
        }
    })
}, function (t, e) {
    var o = {
        config: {
            maxSelectLength: 217,
            apiUrl: "/json/misprint/",
            captchaUrl: "captcha/",
            rulesForSelect: [".post__body_full", ".article__body", ".post__title-text", ".default-block__polling-title", ".poll-result__data-label", ".radio__label_poll"]
        }, show: function () {
            $(".js-typo-container").addClass("tm-typo-form_show"), $(".js-tm-typo-form-textarea").focus()
        }, hide: function () {
            $(".js-typo-container").removeClass("tm-typo-form_show")
        }, isShow: function () {
            return $(".js-typo-container").hasClass("tm-typo-form_show")
        }
    };
    o.loader = {
        loader: $(".js-typo-form-loader"), show: function () {
            this.loader.show()
        }, hide: function () {
            this.loader.hide()
        }
    }, o.clearAndClose = function () {
        $(".js-typo-form").get(0).reset(), $(".js-captcha-place").empty(), o.hide()
    }, o.fillQuote = function (t) {
        $(".js-typo-quote-form").val(t), $(".js-typo-quote").text(t)
    }, o.getPreviewText = function (t) {
        var e, n = $(".js-typo-length-error");
        return t.length < o.config.maxSelectLength ? (n.hide(), e = t) : (n.show(), e = t.slice(0, o.config.maxSelectLength).concat("", "...")), e
    }, o.checkRulesForSelection = function (t) {
        return t.closest(o.config.rulesForSelect)
    }, o.checkSelectInPost = function () {
        var t = document.getSelection(), e = t.anchorNode.parentNode, n = o.checkRulesForSelection(e),
            r = $.trim(t.toString()).length > 0;
        return !!e && !!n && r
    }, o.getCaptcha = function () {
        var t = $("input[name=post_id]").val(), e = o.config.apiUrl + o.config.captchaUrl, n = $(".js-captcha-place");
        if (0 === n.length) return !1;
        o.loader.show(), $.ajax({
            url: e, data: {post_id: t, captcha_type: "habr"}, success: function (t) {
                n.html(t.captcha), o.loader.hide()
            }, error: ajaxFormError, dataType: "json", type: "POST"
        })
    }, o.init = function () {
        o.getCaptcha();
        var t = document.getSelection().toString(), e = o.getPreviewText(t);
        o.fillQuote(e), o.show()
    }, $(document).on("keydown", function (t) {
        (t.metaKey || t.ctrlKey) && 13 === t.keyCode && o.checkSelectInPost() && o.init(), 27 === t.keyCode && o.isShow() && o.clearAndClose()
    }).on("closetypo", function () {
        o.clearAndClose()
    }).on("submit", ".js-typo-form", function (t) {
        t.preventDefault();
        var e = $(".js-form-buttons");
        o.loader.show(), e.hide(), $.ajax({
            url: o.config.apiUrl, data: $(this).serialize(), success: function (t) {
                t.system_errors && t.system_errors.length > 0 ? (show_system_error(t), $("#imgCaptcha").attr("src", "/core/captcha/?" + Math.random())) : ($.jGrowl(window.i18nMsg("POSTS_TYPO_FORM_SUCCESS_SEND"), {theme: "notice"}), o.clearAndClose()), o.loader.hide(), e.show()
            }, error: ajaxFormError, dataType: "json", type: "POST"
        })
    })
}, function (t, e) {
    $(document).ready(function () {
        var t = 1 == window.g_is_enableShortcuts;
        if (t) {
            if ($("input,textarea,select").on("focus", function () {
                t = !1
            }), $("input,textarea,select").on("blur", function () {
                t = !0
            }), $(".shortcuts_items").size()) {
                var e = $(".shortcuts_items .shortcuts_item"), o = $(".main-navbar").height();
                $(window).scroll(function () {
                    var t = window.pageYOffset + 0 + o + 4, n = window.pageYOffset + 50 + o + 4;
                    e.each(function (e, o) {
                        var r = $(o), s = r.offset().top + 20;
                        t < s && s < n && (r.hasClass("focus") || ($(".shortcuts_items .shortcuts_item.focus").removeClass("focus"), r.addClass("focus")))
                    })
                })
            }
            $.each({
                "/": "shortcuts.focus_to_search",
                h: "shortcuts.to_first_post",
                l: "shortcuts.to_last_post",
                j: "shortcuts.to_next_post",
                k: "shortcuts.to_prev_post",
                o: "shortcuts.open_post"
            }, n), $.each({
                c: "shortcuts.to_comment_form",
                t: "shortcuts.add_to_tracker",
                m: "shortcuts.subscribe_comments",
                r: "shortcuts.refresh_comments",
                f: "shortcuts.to_next_unreaded_comment",
                j: "shortcuts.to_next_new_comment",
                k: "shortcuts.to_prev_new_comment",
                esc: "shortcuts.escape"
            }, n), $("input, textarea").bind("keydown", "alt+return ctrl+return", function () {
                var t = $(this).closest("form");
                return $(document).trigger("shortcuts.submit_form", t), !1
            }), $(document).bind("shortcuts.subscribe_comments", function (t, e) {
                $("#subscribe_comments").trigger("click")
            }), $(document).bind("shortcuts.add_to_tracker", function (t, e) {
                $("#tracker_comments").trigger("click")
            }), $(document).bind("shortcuts.focus_to_search", function (t, e) {
                openSearch()
            }), $(document).bind("shortcuts.submit_form", function (t, e) {
                $('input[type="submit"],input.submit,input.edit,button[type="submit"],button[name="send"],button[name="save"]', e).each(function (t, e) {
                    $(e).hasClass("hidden") || e.click()
                })
            }), $(document).bind("shortcuts.to_next_page", function () {
                var t = $("#next_page").attr("href");
                void 0 !== t && (document.location.href = t)
            }), $(document).bind("shortcuts.to_prev_page", function () {
                var t = $("#previous_page").attr("href");
                void 0 !== t && (document.location.href = t)
            }), $(document).bind("shortcuts.to_first_post", function () {
                var t = $(".main-navbar").height(), e = $(".shortcuts_items");
                e.size() && ($(".shortcuts_item", e).first().hasClass("focus") ? $(document).trigger("shortcuts.to_prev_page") : ($(".shortcuts_item.focus", e).removeClass("focus"), $(".shortcuts_item", e).first().addClass("focus")), $.scrollTo($(".shortcuts_item.focus", e), 200, {
                    axis: "y",
                    offset: -1 * t - 4
                }))
            }), $(document).bind("shortcuts.to_last_post", function () {
                var t = $(".main-navbar").height(), e = $(".shortcuts_items");
                e.size() && ($(".shortcuts_item", e).last().hasClass("focus") ? $(document).trigger("shortcuts.to_next_page") : ($(".shortcuts_item.focus", e).removeClass("focus"), $(".shortcuts_item", e).last().addClass("focus")), $.scrollTo($(".shortcuts_item.focus", e), 200, {
                    axis: "y",
                    offset: -1 * t - 4
                }))
            }), $(document).bind("shortcuts.to_next_post", function () {
                var t = $(".main-navbar").height(), e = $(".shortcuts_items");
                if (e.size()) {
                    if (0 == $(".shortcuts_item.focus", e).size()) $(".shortcuts_item", e).first().addClass("focus"); else {
                        var o = $(".shortcuts_item.focus", e), n = o.next();
                        0 == n.size() ? $(document).trigger("shortcuts.to_next_page") : (o.removeClass("focus"), n.addClass("focus"))
                    }
                    $.scrollTo($(".shortcuts_item.focus", e), 200, {axis: "y", offset: -1 * t - 4})
                }
            }), $(document).bind("shortcuts.to_prev_post", function () {
                var t = $(".main-navbar").height(), e = $(".shortcuts_items");
                if (e.size()) {
                    if (0 == $(".shortcuts_item.focus", e).size()) $(".shortcuts_item", e).last().addClass("focus"); else {
                        var o = $(".shortcuts_item.focus", e), n = o.prev();
                        0 == n.size() ? $(document).trigger("shortcuts.to_prev_page") : (o.removeClass("focus"), n.addClass("focus"))
                    }
                    $.scrollTo($(".shortcuts_item.focus", e), 200, {axis: "y", offset: -1 * t - 4})
                }
            }), $(document).bind("shortcuts.open_post", function () {
                if ($(".shortcuts_items").size()) {
                    var t = $(".shortcuts_items .shortcuts_item.focus .post__title_link").attr("href");
                    t && (document.location.href = t)
                }
            }), $(document).bind("shortcuts.to_comment_form", function () {
                var t = $(".main-navbar").height();
                $("#comments_form_placeholder").size() && ($.scrollTo($("#comments_form_placeholder"), 200, {
                    axis: "y",
                    offset: -1 * t - 4
                }), $("#comment_text").focus())
            }), $(document).bind("shortcuts.refresh_comments", function () {
                g_show_xpanel && $("#xpanel .refresh").trigger("click")
            }), $(document).bind("shortcuts.to_next_unreaded_comment", function () {
                g_show_xpanel && $("#xpanel .new").trigger("click")
            }), $(document).bind("shortcuts.to_next_new_comment", function () {
                g_show_xpanel && $("#xpanel .next_new").trigger("click")
            }), $(document).bind("shortcuts.to_prev_new_comment", function () {
                g_show_xpanel && $("#xpanel .prev_new").trigger("click")
            })
        }

        function n(e, o) {
            $(document).bind("keydown", e, function () {
                if (t) return $(document).trigger(o), !1
            })
        }
    })
}, function (t, e) {
    t.exports = function () {
        lastScrollPosition = 0;
        var t = $("#scroll_to_top"), e = 0;
        t.on("click", function (e) {
            e.preventDefault(), t.hasClass("back-down") ? (t.attr("title", window.i18nMsg("SCROLL_TOP")).removeClass("back-down"), $.scrollTo(lastScrollPosition, 700, {axis: "y"}), lastScrollPosition = 0) : (lastScrollPosition = window.pageYOffset, t.attr("title", window.i18nMsg("SCROLL_DOWN")).addClass("back-down"), $.scrollTo(0, 700, {axis: "y"}))
        }), t.toggleClass("hidden", window.pageYOffset < 100), $(window).on("scroll", function () {
            t.toggleClass("hidden", window.pageYOffset < 100), e < window.pageYOffset && t.hasClass("back-down") && t.removeClass("back-down"), e = window.pageYOffset
        })
    }
}, , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , function (t, e, o) {
    o(142), o(309), o(306), t.exports = o(310)
}, function (t, e, o) {
    var n = o(40), r = o(24);
    window.ajaxFormBeforSubmit = n.ajaxFormBeforSubmit, window.ajaxFormError = n.ajaxFormError, window.ajaxFormRedirect = n.ajaxFormRedirect, window.ajaxFormSuccess = n.ajaxFormSuccess, window.createCookie = n.createCookie, window.empty = n.empty, window._getDate = n._getDate, window._getTime = n._getTime, window.H = n.H, window.K = n.K, window.mention_autocomplete = n.mention_autocomplete, window.replaceURLWithHTMLLinks = n.replaceURLWithHTMLLinks, window.show_form_errors = n.show_form_errors, window.show_system_error = n.show_system_error, window.timer_countdown = n.timer_countdown, window.userKarmaVote = n.userKarmaVote, window.copyCurrentUrl = n.copyCurrentUrl, window.i18nMsg = r.msg;
    var s = o(41), a = o(42);
    window.RecaptchaOptions = s.RecaptchaOptions, window.show_uploader = s.show_uploader, window.openSearch = a.openSearch, o(143), o(144), o(145), o(59), o(146), o(147), o(148), o(79), o(43), o(149), o(150), o(151), o(155), o(159), o(163), o(171), o(174), o(179), o(78), o(180), o(61), o(44);
    var i = o(45), c = o(181), l = o(80), u = (o(182), o(183));
    $(window).load(function () {
        i()
    }), $(document).ready(function () {
        var t;
        c($(".sidebar_right_stickable")), t = $(".corporate_blog").length ? ".corporate_blog" : ".daily_best_posts", $(t).length && u(t), l()
    })
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        var t = $(".js-confidantial_check"), e = $('#feedback_form button[name="submit"]');
        t && t.on("click", function () {
            e.attr("disabled", !0), $(this).is(":checked") && e.attr("disabled", !1)
        }), e.on("click", function () {
            $("#feedback_form").ajaxForm({
                form: $("#feedback_form"),
                url: "/json/feedback/save/",
                beforeSubmit: function (t, e, o) {
                    return $("#preview_placeholder").html("").addClass("hidden"), n.ajaxFormBeforSubmit(t, e, o)
                },
                error: n.ajaxFormError,
                success: n.ajaxFormSuccess(function (t, e, o, n) {
                    $("#feedback_form").html('<div class="global_notify"><div class="inner_notice">' + window.i18nMsg("FEEDBACK_FORM_SUCESS_MESSAGE") + "</div></div>")
                }, function (t, e, o, n) {
                    $("#imgCaptcha").size() && ($("#imgCaptcha").attr("src", "/core/captcha?" + Math.random()), $("#captcha").val(""))
                })
            })
        })
    })
}, function (t, e) {
    $(window).on("scroll", function () {
        var t = $('[data-name="sidebarToolkitSections"]'), e = $(t).find(".default-block"),
            o = $(".layout__row_footer-links");
        if (t.length && e.length) {
            var n = $(e).outerHeight() + 30, r = ($(e).offset().top, $(t).offset().top, o.offset().top),
                s = "is-relative", a = "is-fixed", i = "is-absolute";
            $(window).scrollTop() + n >= r ? c(i) : e.hasClass("sticked") ? c(a) : c(s)
        }

        function c(t) {
            var o = [s, a, i], n = o.filter(function (e) {
                return e === t
            }).join(""), r = o.filter(function (e) {
                return e !== t
            }).join(" ");
            e.addClass(n), e.removeClass(r)
        }
    })
}, function (t, e, o) {
    var n = o(34), r = o(32);
    $(document).ready(function () {
        "use strict";
        new r({
            selectors: {
                clear: "#hubs_suggest_clear",
                container: "#hubs",
                suggest: "#hubs_suggest"
            }
        }), new n({
            selectors: {follow: ".js-hub-follow-btn"}, suggestUrl: "/json/hubs/", suggestRequest: function (t) {
                return {hub_id: $(t).attr("data-id")}
            }
        })
    })
}, function (t, e) {
    var o = "stick-top", n = "stick-bottom", r = "margin", s = 0;

    function a(t, e, a, l, u) {
        var d = e.scrollHeight, _ = window.innerHeight, m = 0, f = 0;
        if (a && a.length && [].slice.call(a.children).forEach(t => {
            f += t.scrollHeight
        }), [].slice.call(t.children).forEach(t => {
            m += t.scrollHeight
        }), d <= _ || d + f > m) return a || c(l, "sidebar", r, o), c(e, "sidebar_right", r, o), void c(a, "sidebar_right_ad", r, o);
        var p = window.scrollY, h = s > p, g = s < p, E = e.getBoundingClientRect().y, T = d + E;
        window.scrollsState && window.scrollsState[u] && (g && (window.scrollsState[u].scrollMode === r && T < _ && (window.scrollsState[u].sidebarMargin = 0, window.scrollsState[u].scrollMode = n, a || c(l, "sidebar", r, n), i(e, r, window.scrollsState[u]), c(a, "sidebar_right_ad", r, n)), window.scrollsState[u].scrollMode === o && (window.scrollsState[u].sidebarMargin = e.offsetTop, window.scrollsState[u].scrollMode = r, a || c(l, "sidebar", o, r), i(e, o, window.scrollsState[u]), c(a, "sidebar_right_ad", o, r))), h && (window.scrollsState[u].scrollMode === n && (window.scrollsState[u].sidebarMargin = e.offsetTop, window.scrollsState[u].scrollMode = r, a || c(l, "sidebar", n, r), i(e, n, window.scrollsState[u]), c(a, "sidebar_right_ad", n, r)), window.scrollsState[u].scrollMode === r && E >= 0 && (window.scrollsState[u].sidebarMargin = 0, window.scrollsState[u].scrollMode = o, a || c(l, "sidebar", r, o), i(e, r, window.scrollsState[u]), c(a, "sidebar_right_ad", r, o)))), s = p
    }

    function i(t, e, o) {
        t.style.marginTop = o.sidebarMargin + "px", c(t, "sidebar_right", e, o.scrollMode)
    }

    function c(t, e, o, n) {
        t && t.classList && (o && t.classList.remove(e + "_" + o), t.classList.add(e + "_" + n))
    }

    window.scrollsState = [], $(document).ready(function () {
        var t = document.querySelector("div.layout__cell_body").querySelectorAll(".column-wrapper");
        [].slice.call(t).forEach((t, e) => {
            var n = t.querySelector("div.sidebar");
            if (n) {
                var r = t.querySelector("div.content_left"), s = n.querySelector("div.sidebar_right"),
                    i = n.querySelector("div.sidebar_right_ad");
                r && s && (window.scrollsState.push({
                    sidebarMargin: 0,
                    scrollMode: o
                }), a(r, s, i, n, e), window.addEventListener("scroll", () => a(r, s, i, n, e), {passive: !0}))
            }
        })
    })
}, function (t, e) {
    var o = "https://effect.habr.com/api/inv/pa";

    function n(t, e, n, r) {
        e && e.length && $.ajax({
            url: o,
            data: {area: "habr", count: t, ll: window.g_current_hl, al: window.g_current_fl},
            success: function (t) {
                t.adverts && t.adverts.length && (t.adverts.forEach(t => {
                    e.append(composeTeaser(t, r))
                }), n.show())
            },
            type: "GET"
        })
    }

    $(document).ready(function () {
        n(3, $("#megapost-teasers"), $("#effect"), "feed_middle"), n(2, $("#megapost-teasers-sidebar"), $("#effect-sidebar"), "feed_bottom")
    })
}, function (t, e) {
    $(document).ready(function () {
        $("#filter"), $("#list")
    })
}, function (t, e) {
    $(document).ready(function () {
        $('#search_form input[type="text"]').autocomplete({
            serviceUrl: "/json/suggest/",
            minChars: 2,
            delimiter: /(,|;)\s*/,
            maxHeight: 400,
            width: 300,
            zIndex: 9999,
            deferRequestBy: 500,
            params: {type: "search"}
        })
    })
}, function (t, e) {
    $(document).ready(function () {
        var t, e, o, n, r,
            s = (t = "ab_test_vacancies_block_group", (e = $.cookie(t)) || (o = 0, n = 1, r = Math.floor(Math.random() * (n - o + 1)) + o, e = ["A", "B"][r], $.cookie(t, e, {
                expires: 14,
                path: "/"
            }), e));
        $(".promo-block_vacancies a").each(function () {
            var t = $(this), e = t.data("utm"), o = "utm_campaign=tm_promo_" + s,
                n = e.replace("utm_campaign=tm_promo", o);
            t.data("utm", n), t.attr("data-utm", n)
        })
    })
}, function (t, e, o) {
    o(152), o(153), o(154)
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        $("#add_director").click(function () {
            var t = $("#directors_list .row").size(),
                e = $('<div class="row directors_' + t + '" id="row_' + t + '">                <div class="td name"><input type="text" name="directors[' + t + '][name]" value=""/></div>                <div class="td position"><input type="text" name="directors[' + t + '][position]" value=""/></div>                <div class="td delete"><a href="#remove" data-id="' + t + '" class="dotted">' + window.i18nMsg("REMOVE_ACTION_BUTTON_TEXT") + '</a></div>                <div class="clear"></div>                <div class="error"></div>              </div>');
            return $("#directors_list").append(e), !1
        }), $("#directors_list .row .delete a").on("click", function () {
            var t = $(this).data("id");
            return $("#row_" + t).html("").hide(), !1
        }), $("#add_stage").click(function () {
            var t = $("#stages_list .row").size() + 1, e = $("#stage_select_date_template").html();
            e = e.replace(/NUM/g, t);
            var o = $('<div class="row" id="row_' + t + '">                <div class="td text"><input type="text" name="stages[' + t + '][description]" value=""/></div>                <div class="td date">' + e + '</div>                <div class="td delete"><a href="#remove" data-id="' + t + '" class="dotted">удалить</a></div>              </div>');
            return $("#stages_list").append(o), !1
        }), $("#stages_list .row .delete a").on("click", function () {
            var t = $(this).data("id");
            return $("#row_" + t).html("").hide(), !1
        }), $("#company_add_form").ajaxForm({
            form: $("#company_add_form"),
            beforeSubmit: n.ajaxFormBeforSubmit,
            error: n.ajaxFormError,
            success: n.ajaxFormSuccess(ajaxFormRedirect)
        }), $("#startup_add_form").ajaxForm({
            beforeSubmit: n.ajaxFormBeforSubmit,
            error: n.ajaxFormError,
            success: n.ajaxFormSuccess(function (t) {
                "ok" == t.messages && ($(".company_add").hide(), $(".result").show(), $("html, body").animate({scrollTop: $(".result").offset().top + "px"}))
            })
        })
    })
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        $("#i_work_in_company").click(function () {
            var t = $(this), e = $(this).data("id"), o = $("#js-companyWorkersCount"), r = $("#js-companyWorkersList");
            t.attr("checked") ? $.post("/json/corporation/worker_add/", {company_id: e}, function (e) {
                "ok" == e.messages ? (t.attr("title", window.i18nMsg("COMPANY_QUIT_ACTION")), $.jGrowl(window.i18nMsg("COMPANY_MEMBER_YOU")), o.text(parseInt(o.text()) + 1), r.append('<li><a href="http://' + g_user_login + "." + g_base_url + '/" class="you">' + g_user_login + "</a></li>")) : n.showSystemError(e)
            }, "json") : $.post("/json/corporation/worker_del/", {company_id: e}, function (e) {
                "ok" == e.messages ? (t.attr("title", window.i18nMsg("COMPANY_MEMBER_I")), $.jGrowl(window.i18nMsg("COMPANY_DONT_MEMBER")), o.text(parseInt(o.text()) - 1), $(".you", r).remove()) : n.showSystemError(e)
            }, "json")
        }), $("#toggle_company_apps").on("click", function () {
            return $("#company-wjt .content-list__item_company-wjt.hidden").show(), $(this).parent().remove(), !1
        })
    })
}, function (t, e, o) {
    var n = o(34), r = o(32);
    $(document).ready(function () {
        "use strict";
        new r({
            selectors: {
                clear: "#companies_suggest_clear",
                container: "#companies",
                suggest: "#companies_suggest"
            }
        }), new n({
            selectors: {follow: ".js-company-follow-btn"},
            suggestUrl: "/json/corporation/",
            suggestRequest: function (t) {
                return {company_id: $(t).attr("data-id")}
            }
        })
    })
}, function (t, e, o) {
    o(156), o(158)
}, function (t, e, o) {
    var n = o(2), r = o(157);
    $(document).ready(function () {
        var t = $("#conversation_form"), e = t.find("button[name=send]");
        $(e).on("click", function () {
            $(this).addClass("loading")
        }), t.ajaxForm({
            form: t,
            beforeSubmit: n.ajaxFormBeforSubmit,
            error: n.ajaxFormError,
            success: n.ajaxFormSuccess(function (t, o, n, r) {
                var s = $(t.message_html);
                $("#messages").prepend(s), $("#conversation_form textarea").val(""), $(document).trigger("hljsUpdate"), $("#preview_placeholder").html("").addClass("hidden"), $(".item.captcha").addClass("hidden"), $("#conversation_form textarea").focus(), e.removeClass("loading")
            }, function (t, e, o, n) {
                "undefined" != typeof Recaptcha && Recaptcha.reload(), $("#imgCaptcha").size() && ($("#imgCaptcha").attr("src", "/core/captcha?" + Math.random()), $("#captcha").val(""))
            })
        }), $("textarea", t).on("keyup change blur focus", function (e) {
            $(this).val().length > 0 ? $(".js-form-buttons > button", t).attr("disabled", !1) : $(".js-form-buttons > button", t).attr("disabled", !0)
        }), r($("#conversation_form textarea")), $(".js-btn_preview").on("click", function () {
            t.ajaxSubmit({
                form: t,
                url: "/json/conversations/preview/",
                beforeSubmit: n.ajaxFormBeforSubmit,
                error: n.ajaxFormError,
                success: n.ajaxFormSuccess(function (t, e, o, n) {
                    var r = $(t.message_html);
                    $("#preview_placeholder").html(r).removeClass("hidden"), $(document).trigger("hljsUpdate")
                })
            })
        }), $("#load_more").click(function () {
            var t = $(this);
            return t.addClass("loading"), $.post(t.data("url"), {
                conversation_id: t.data("id"),
                respondent: t.data("respondent"),
                offset: t.data("offset")
            }, function (e) {
                if ("ok" == e.messages) {
                    for (k in e.items) if (0 == $("#message_" + e.items[k].id).size()) {
                        var o = $(e.items[k].html);
                        o.hide(), $("#messages ").append(o), o.fadeIn(500)
                    }
                    0 != e.has_more ? t.data("offset", e.offset) : t.remove()
                } else n.showSystemError(e);
                t.removeClass("loading")
            }), !1
        })
    })
}, function (t, e) {
    t.exports = function (t) {
        t.textcomplete([{
            match: /(^|\s)@([-\w]*)$/, template: function (t) {
                return '<img src="' + t.avatar + '" alt="" /> <span class="name">' + t.value + "</span>"
            }, search: function (t, e) {
                t.length >= 3 ? $.getJSON("/json/mentions/", {term: t}).done(function (t) {
                    e(t.results)
                }).fail(function () {
                    e([])
                }) : e([])
            }, replace: function (t) {
                return "$1@" + t.value + " "
            }
        }])
    }
}, function (t, e, o) {
    var n = o(2), r = o(32);
    $(document).ready(function () {
        "use strict";
        new r({
            selectors: {
                clear: "#people_suggest_clear",
                container: "#conversations",
                suggest: "#conversations_suggest"
            }
        }), $(".js-delete-conversation").on("click", function (t) {
            t.preventDefault();
            var e = $(this).data("id");
            confirm(window.i18nMsg("CONVERSATION_REMOVE_NOTICE")) && $.post("/json/conversations/delete/", {conversation_id: e}, function (t) {
                "ok" == t.messages ? $("#conversation_" + e).parent().remove() : n.showSystemError(t)
            })
        })
    })
}, function (t, e, o) {
    o(160), o(161), o(162);
    var n = o(46), r = o(60);
    window.change_post_subscibptions_checkboxes = n.change_post_subscibptions_checkboxes, window.closeForm = n.closeForm, window.posts_add_to_favorite = n.posts_add_to_favorite, window.posts_draft_delete = n.posts_draft_delete, window.posts_poll = r.vote, window.posts_vote_minus = n.posts_vote_minus, window.posts_vote_plus = n.posts_vote_plus, window.posts_vote_result = n.posts_vote_result, window.posts_list_vote_result = n.posts_list_vote_result, window.showAbuseForm = n.showAbuseForm, window.show_edit_tags = n.show_edit_tags, window.show_recommend_form = n.show_recommend_form
}, function (t, e, o) {
    var n = o(2);

    function r(t) {
        $("body").toggleClass("overlayed", t), $("#js-donate").toggleClass("hidden", !t)
    }

    $(document).ready(function () {
        var t = $.jStorage.get("last_navigator_category"), e = $.jStorage.get("last_navigator_hubs"),
            o = $.jStorage.get("last_navigator_hub");
        if (t && "null" != t) {
            if ($('#fast_navagator select[name="category"]').val(t), e) {
                var s = "";
                for (k in e) s += '<option value="' + e[k].alias + '">' + e[k].name + "</option>";
                $('#fast_navagator select[name="hub"]').html(s).attr("disabled", !1), $('#fast_navagator input[type="submit"]').attr("disabled", !1)
            }
            o && ($('#fast_navagator select[name="hub"]').val(o), $("#fast_navagator .buttons input").attr("disabled", !1).removeClass("loading"))
        } else $('#fast_navagator select[name="hub"]').html("<option>" + window.i18nMsg("SELECT_DEFAUL_OPTION_HUB") + "</option>").attr("disabled", !0), $('#fast_navagator input[type="submit"]').attr("disabled", !0);
        $('#fast_navagator select[name="category"]').change(function () {
            var t = $(this).val();
            $.jStorage.set("last_navigator_category", t), "null" == t ? ($('#fast_navagator select[name="hub"]').html("<option>" + window.i18nMsg("SELECT_DEFAUL_OPTION_HUB") + "</option>").attr("disabled", !0), $('#fast_navagator input[type="submit"]').attr("disabled", !0)) : $.post("/json/hubs/category/" + t + "/", function (t) {
                if ("ok" == t.messages) {
                    var e = "";
                    for (k in t.hubs) e += '<option value="' + t.hubs[k].alias + '">' + t.hubs[k].name + "</option>";
                    $.jStorage.set("last_navigator_hubs", t.hubs), $('#fast_navagator select[name="hub"]').html(e).attr("disabled", !1), $('#fast_navagator input[type="submit"]').attr("disabled", !1)
                } else n.showSystemError(t)
            }, "json")
        }), $("#fast_navagator").submit(function () {
            var t = $('#fast_navagator select[name="hub"]').val();
            return $.jStorage.set("last_navigator_hub", t), document.location.href = "/hub/" + t + "/", !1
        }), $("#addCompanyFan").click(function () {
            var t = $(this).attr("data-id"), e = $(this);
            return e.addClass("loading"), $.post("/json/corporation/fan_add/", {company_id: t}, function (t) {
                "ok" == t.messages ? ($("#removeCompanyFan").removeClass("hidden"), $("#addCompanyFan").addClass("hidden"), $("#fans_count").text(t.fans_count_str)) : n.showSystemError(t), e.removeClass("loading")
            }, "json"), !1
        }), $("#removeCompanyFan").click(function () {
            var t = $(this).attr("data-id"), e = $(this);
            return e.addClass("loading"), $.post("/json/corporation/fan_del/", {company_id: t}, function (t) {
                "ok" == t.messages ? ($("#removeCompanyFan").addClass("hidden"), $("#addCompanyFan").removeClass("hidden"), $("#fans_count").text(t.fans_count_str)) : n.showSystemError(t), e.removeClass("loading")
            }, "json"), !1
        }), $(".js-show-donate").on("click", function (t) {
            r(!0)
        }), $(".js-hide-donate").on("click", function (t) {
            r(!1)
        }), $(document).on("keyup", function (t) {
            27 === t.which && r(!1)
        }), $("#js-donate").on("click", function (t) {
            $(t.target).closest(".popup").length || r(!1)
        }), $(".post__text_v2 .formula").contextmenu(function (t) {
            t.preventDefault(), $("#formula-context-menu").remove();
            var e = this.getAttribute("source");
            $("<div>", {
                id: "formula-context-menu",
                class: "formula-context-menu",
                style: `top: ${t.pageY + 10}px; left: ${t.pageX}px`
            }).appendTo(".layout"), $("<div>", {
                class: "formula-context-menu__item",
                id: "formula-copy",
                "data-copy": e
            }).text(window.i18nMsg("COPY_TEX_CODE")).appendTo("#formula-context-menu")
        }), $(document).on("click", function (t) {
            var e, o, n = "#formula-copy";
            $(t.target).closest(n).length ? (e = $(n).attr("data-copy"), o = $("<input>"), $("body").append(o), o.val(e).select(), document.execCommand("copy"), o.remove(), $.jGrowl(window.i18nMsg("COPIED")), $("#formula-context-menu").remove()) : $("#formula-context-menu").length && $("#formula-context-menu").remove()
        })
    })
}, function (t, e, o) {
    var n = o(2);
    $(function () {
        if (!$("#habralenta-settings").length) return !1;
        var t = $('<img src="/images/form/ajax_loader.gif" alt="" class="ajax_loader" align="top"/>'),
            e = $("#hubs_category"),
            o = ($("#habralenta-settings"), $("#form_habralenta_settings"), $("#save_success"), ""),
            r = window.hubs_category_json;
        $.each(r.categories, function (t, e) {
            var n = 2 == e.subscription ? "full" : "", s = 1 == e.subscription ? "part" : "",
                a = 2 == e.subscription ? "Отписаться от всех хабов в этом потоке" : "Подписаться на все хабы в этом потоке",
                i = "corporative_blogs" == e.alias ? "/companies/" : "/hubs/" + e.alias + "/",
                c = r.lenta_show_all || 0 == e.count ? "disabled" : "",
                l = e.have_new ? ' <span class="new">' + e.have_new + "</span>" : "",
                u = e.count > 0 ? '<a href="' + i + '" title="Нажмите, чтобы посмотреть хабы из этого потока">' + e.name + "</a>" : '<span class="category_title">' + e.name + "</span>";
            o += '<div class="category ' + n + " " + s + " " + c + '" data-alias="' + e.alias + '">                       <div class="checkbox" title="' + a + '"></div>                       <input type="hidden" name="categories[' + e.alias + ']" class="input" value="' + e.subscription + '" />                       <div class="title">                         ' + u + '                         <span class="count" title="Количество хабов в категории">(' + e.count + ")</span>                         " + l + '                       </div>                       <div class="hubs"></div>                     </div>'
        }), e.html(o), $(".lenta-tip").tipTip({
            maxWidth: "300",
            edgeOffset: 10
        }), void 0 !== r.subscription && r.subscription && ($('input[name="enable_subscription"]').prop("checked", !0), $('select[name="subscription_frequency"]').val(r.subscription.frequency)), $(".category .title a", e).on("click", function () {
            var e = $(this).parent().parent(".category"), o = $(".hubs", e), n = $(".title .count", e),
                r = e.attr("data-alias");
            return !e.hasClass("disabled") && (o.html() ? o.toggleClass("show") : (n.after(t), $.get("/json/hubs/category/" + r + "/", function (n) {
                var s = "";
                $.each(n.hubs, function (t, o) {
                    e.hasClass("full") ? o.subscription = 1 : e.hasClass("part") || (o.subscription = 0);
                    var n = o.subscription ? "subscription" : "",
                        a = o.subscription ? "Отписаться от хаба" : "Подписаться на хаб",
                        i = "corporative_blogs" == r ? "/company/" + o.alias + "/blog/" : "/hub/" + o.alias + "/",
                        c = o.is_new ? ' <span class="new">новый</span>' : "";
                    s += '<div class="hub ' + n + '">                         <div class="checkbox" title="' + a + '"></div>                         <input type="hidden" name="blogs[' + o.id + ']" class="input" value="' + o.subscription + '" />                         <a href="' + i + '" target="_blank">' + o.name + "</a>                         " + c + "                       </div>"
                }), t.remove(), o.html(s), o.toggleClass("show"), $(".hub a", o).click(function () {
                    if ($(this).parent(".hub").parent().parent(".category").hasClass("disabled")) return !1
                }), $(".hub .checkbox", o).on("click", function (t) {
                    t.preventDefault();
                    var e = $(this), n = e.parent(".hub"), r = n.parent().parent(".category"), s = $(".input", n),
                        a = $("> .input", r);
                    if (r.hasClass("disabled")) return !1;
                    n.hasClass("subscription") ? (n.removeClass("subscription"), s.val(0), e.attr("title", "Подписаться на хаб")) : (n.addClass("subscription"), s.val(1), e.attr("title", "Отписаться от хаба"));
                    var i = $(".subscription", o), c = $(".hub", o);
                    0 == i.size() ? (r.removeClass("part").removeClass("full"), a.val(0), r.find("> .checkbox").attr("title", "Подписаться на все хабы в этой категории")) : i.size() == c.size() ? (r.removeClass("part").addClass("full"), a.val(2), r.find("> .checkbox").attr("title", "Отписаться от всех хабов в этой категории")) : (r.removeClass("full").addClass("part"), a.val(1), r.find("> .checkbox").attr("title", "Подписаться на все хабы в этой категории"))
                })
            }, "json")), !1)
        }), $(".category > .checkbox", e).on("click", function () {
            var t = $(this), e = t.parent(".category"), o = (e.attr("data-alias"), $(".hubs", e)), n = $(".input", e);
            return !e.hasClass("disabled") && (e.hasClass("full") ? (e.removeClass("full").removeClass("part"), n.val(0), $(".hub", o).removeClass("subscription"), $(".hub .input", o).val(0), t.attr("title", "Подписаться на все хабы в этой категории")) : e.hasClass("part") ? (e.removeClass("part").addClass("full"), n.val(2), $(".hub", o).addClass("subscription"), $(".hub .input", o).val(1), t.attr("title", "Отписаться от всех хабов в этой категории")) : (e.addClass("full"), n.val(2), $(".hub", o).addClass("subscription"), $(".hub .input", o).val(1), t.attr("title", "Отписаться от всех хабов в этой категории")), !1)
        }), $("#form_habralenta_settings").ajaxForm({
            form: $("#form_habralenta_settings"),
            url: "/json/hubs/save_subscriptions/",
            beforeSubmit: n.ajaxFormBeforSubmit,
            error: n.ajaxFormError,
            success: n.ajaxFormSuccess(function (t, e, o, n) {
                $.jGrowl("Настройки ленты успешно сохранились")
            })
        })
    })
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        $('button[name="send_invite"]').on("click", function () {
            var t = $(this), e = $(this).data("id");
            return t.addClass("loading"), $.post("/json/users/give_invite_bypost/", {post_id: e}, function (o) {
                "ok" == o.messages ? $("#sandbox-panel_" + e + " .send_invite_wrapper").html('<span style="color:green;">' + window.i18nMsg("INVITE_WAS_SENT") + "</span>") : n.showSystemError(o), t.removeClass("loading")
            }, "json"), !1
        }), $('button[name="send_invite_plus"]').on("click", function () {
            if (confirm(window.i18nMsg("CONVERSATION_INVITE_USER"))) {
                var t = $(this), e = $(this).data("id");
                t.addClass("loading"), $.post("/json/users/give_invite_bypost_plus/", {post_id: e}, function (o) {
                    "ok" == o.messages ? $("#sandbox-panel_" + e + " .send_invite_wrapper").html('<span style="color:green;">' + window.i18nMsg("INVITE_WAS_SENT") + "</span>") : n.showSystemError(o), t.removeClass("loading")
                }, "json")
            }
            return !1
        }), $('button[name="reject"]').on("click", function () {
            var t = $(this).data("id");
            $("#reject_form_post_" + t).removeClass("hidden"), $("#transfer_form_post_" + t).addClass("hidden");
            var e = $("#reject_form_post_" + t + ' select[name="reject_reason"]');
            e.attr("enabled", "true");
            const o = $("#reject_form_post_" + t).data("locale");
            return $.each(reasonsList[o], function () {
                e.append($("<option></option>").text(this.optionValue).val(this.optionValue).attr({
                    "data-text": this.textareaValue,
                    "data-action": this.action,
                    "data-cause": this.cause
                }))
            }), !1
        }), $(document).on("change", '.reject_form select[name="reject_reason"]', function (t) {
            var e = $(this).closest(".reject_form").data("id"), o = this.options[t.target.selectedIndex],
                n = $(o).data("text"), r = $(o).data("action");
            $("#reject_form_post_" + e).find("#reject_text_textarea").val(n), $("#reject_form_post_" + e).find('input[name="reject_action"]').val(r), $("#reject_form_post_" + e).find('input[name="reject_cause"]').val($(o).data("cause")), "" === r && $("#reject_form_post_" + e + " .reject_reason").find(".error").text(window.i18nMsg("REJECT_REASON_DOESNT_CHOISE"))
        }), $('button[name="transfer"]').on("click", function () {
            var t = $(this).data("id");
            return $("#reject_form_post_" + t).addClass("hidden"), $("#transfer_form_post_" + t).removeClass("hidden"), !1
        }), $('button[name="publish"]').on("click", function (t) {
            var e = $(t.target), o = e.data("id");
            e.addClass("loading");
            var r = {post_id: o};
            return $.post("/json/sandbox/publish/", r, function (t) {
                "ok" === t.messages ? $("#sandbox-panel_" + o).html('<div class="send_invite_wrapper"><span style="color:green;">Опубликовано в песочнице</span></div>') : n.showSystemError(t), e.removeClass("loading")
            }, "json"), !1
        })
    })
}, function (t, e, o) {
    o(164), o(165), o(166), o(167), o(168), o(169), o(170)
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        $("#password_settings_form").ajaxForm({
            dataType: "json",
            url: "/json/settings/password/ ",
            beforeSubmit: function () {
                $("#password_settings_form .error").text("").hide(), $('#password_settings_form input[type="submit"]').addClass("loading")
            },
            success: function (t) {
                "ok" == t.messages ? $.jGrowl(window.i18nMsg("ACCOUNT_FORM_SETTING_SUCESS_MESSAGE")) : (n.showFormErrors(t), $.scrollTo($("#password_settings_form"), 800, {axis: "y"})), $('#password_settings_form input[type="submit"]').removeClass("loading")
            }
        }), $("#email_settings_form").ajaxForm({
            dataType: "json",
            url: "/json/settings/email/",
            beforeSubmit: function () {
                $("#email_settings_form .error").text("").hide(), $('#email_settings_form input[type="submit"]').addClass("loading")
            },
            success: function (t) {
                "ok" == t.messages ? $.jGrowl(window.i18nMsg("ACCOUNT_FORM_SETTING_SUCESS_MESSAGE")) : (n.showFormErrors(t), $.scrollTo($("#email_settings_form"), 800, {axis: "y"})), $('#email_settings_form input[type="submit"]').removeClass("loading")
            }
        })
    })
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        if (!$("#notifications_settings_form").length) return !1;
        $("#notifications_settings_form").ajaxForm({
            dataType: "json",
            url: "/json/settings/notifications/ ",
            beforeSubmit: function () {
                $("#notifications_settings_form .error").text("").hide(), $('#notifications_settings_form input[type="submit"]').addClass("loading")
            },
            success: function (t) {
                "ok" == t.messages ? $.jGrowl("Новые настройки успешно сохранились") : (n.show_form_errors(t), $.scrollTo($("#notifications_settings_form"), 800, {axis: "y"})), $('#notifications_settings_form input[type="submit"]').removeClass("loading")
            }
        }), void 0 !== json.subscription && json.subscription && ($('input[name="enable_subscription"]').prop("checked", !0), $('select[name="subscription_frequency"]').val(json.subscription.frequency))
    })
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        $("#other_settings_form").ajaxForm({
            form: $("#other_settings_form"),
            url: "/json/settings/others/",
            beforeSubmit: n.ajaxFormBeforSubmit,
            error: n.ajaxFormError,
            success: n.ajaxFormSuccess(function (t, e, o, n) {
                $.jGrowl("Новые настройки успешно сохранились")
            })
        })
    })
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        $("#privacy_settings_form").ajaxForm({
            dataType: "json",
            url: "/json/settings/privacy/ ",
            beforeSubmit: function () {
                $("#privacy_settings_form .error").text("").hide(), $('#privacy_settings_form input[type="submit"]').addClass("loading")
            },
            success: function (t) {
                "ok" == t.messages ? $.jGrowl("Новые настройки успешно сохранились") : (n.show_form_errors(t), $.scrollTo($("#privacy_settings_form"), 800, {axis: "y"})), $('#privacy_settings_form input[type="submit"]').removeClass("loading")
            }
        })
    })
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        $(document).on("click", "#add_contact", function () {
            var t = $("#contacts_list .row").size() + 1, e = $("#contact_select_date_template").html();
            e = e.replace(/NUM/g, t);
            var o = $('<div class="row contacts' + t + '" id="row_' + t + '">                <div class="td type">' + e + '</div>                <div class="td value"><input type="text" name="contacts[' + t + '][value]" value=""/></div>                <div class="td checkbox"><input type="checkbox" class="js-contacts-show_on_post" name="contacts[' + t + '][show_on_post]" title="Показывать в инфопанели на странице публикации"/></div>                <div class="td delete"><a href="#remove" data-id="' + t + '" class="dotted">удалить</a></div>                <div class="error"></div>              </div>');
            return $("#contacts_list").append(o), !1
        });
        $(document).on("change", "input.js-contacts-show_on_post", function (t) {
            $("input.js-contacts-show_on_post:checked").length > 5 && (this.checked = !1)
        }), $("#contacts_list .row .delete a").on("click", function () {
            var t = $(this).data("id");
            return $("#contacts_list #row_" + t).html("").hide(), !1
        }), $("#contacts_list .row .type select").on("change", function () {
            var t = $(this).parents(".row");
            18 == $(this).val() ? $('<div class="row_description">Связь аккаунта на Хабре с учётной записью в Google+ позволит вам числиться автором статей в поисковой выдаче.<br>Подробности читайте в <a href="https://habr.com/docs/help/settings/#2google">справочном разделе</a>.</div>').appendTo(t) : $(".row_description", t).remove()
        }), $("#profile_settings_form").ajaxForm({
            form: $("#profile_settings_form"),
            beforeSubmit: n.ajaxFormBeforSubmit,
            error: n.ajaxFormError,
            success: n.ajaxFormSuccess(function (t, e, o, n) {
                $.jGrowl("Новые настройки успешно сохранились")
            })
        }), $("#avatar_settings_form").ajaxForm({
            form: $("#avatar_settings_form"),
            beforeSubmit: n.ajaxFormBeforSubmit,
            error: n.ajaxFormError,
            success: n.ajaxFormSuccess(function (t, e, o, n) {
                $.jGrowl("Новые настройки успешно сохранились")
            })
        })
    })
}, function (t, e) {
    $(document).ready(function () {
        $(".connect-service").each(function (t, e) {
            $.post($(e).data("url"), {ajax: !0}, function (t) {
                t.id && $(".about", e).html('<a href="' + t.link + '" class="pic"><img src="' + t.pic + '" alt="" /></a><a href="' + t.link + '" class="name">' + t.name + "</a>"), t.auth_error && ($(".about", e).html('<span class="auth_error">' + t.error_message + "</span>"), $("form", e).attr("action", t.connect_url), $("form button", e).text(window.i18nMsg("SOCIAL_PAGE_CONNECT_ACTION")))
            })
        })
    })
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        $("#upgrade_settings_form").ajaxForm({
            form: $("#upgrade_settings_form"),
            beforeSubmit: n.ajaxFormBeforSubmit,
            error: n.ajaxFormError,
            url: "/json/settings/upgrade/ ",
            success: n.ajaxFormSuccess(function (t, e, o, n) {
                return $.jGrowl(window.i18nMsg("INVITE_WAS_ACTIVED")), window.location = "/", !1
            }, function (t, e, o, n) {
                $("#imgCaptcha").size() && ($("#imgCaptcha").attr("src", "/core/captcha?" + Math.random()), $("#captcha").val(""))
            })
        })
    })
}, function (t, e, o) {
    o(172), o(173)
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        $(".app_title").on("click", function () {
            var t = $(this).data("key");
            $("#text-" + t).toggle("slow"), $.post("/json/tracker/apps/mark_as_read/", {single: !0, key: t})
        }), $("#check_all").click(function () {
            $(".tracker_apps td input:checkbox").attr("checked", $(this).prop("checked")).trigger("change")
        }), $(".tracker_apps td input:checkbox").change(function () {
            $(".tracker_apps td input:checked").length > 0 ? $("#remove_button, #mark_read_button").attr("disabled", !1) : $("#remove_button, #mark_read_button").attr("disabled", !0)
        }), $("#remove_button").on("click", function () {
            return $("#tracker_apps_form").ajaxSubmit({
                form: $("#tracker_apps_form"),
                url: "/json/tracker/apps/remove/",
                beforeSubmit: n.ajaxFormBeforSubmit,
                error: n.ajaxFormError,
                success: n.ajaxFormSuccess(n.ajaxFormRedirect)
            }), !1
        }), $("#mark_read_button").on("click", function () {
            return $("#tracker_apps_form").ajaxSubmit({
                form: $("#tracker_apps_form"),
                url: "/json/tracker/apps/mark_as_read/",
                beforeSubmit: n.ajaxFormBeforSubmit,
                error: n.ajaxFormError,
                success: n.ajaxFormSuccess(n.ajaxFormRedirect)
            }), !1
        })
    })
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        $("#check_all").click(function () {
            $(".tracker_comments td input:checkbox").attr("checked", $(this).prop("checked")).trigger("change")
        }), $(".tracker_comments td input:checkbox").change(function () {
            $(".tracker_comments td input:checked").length > 0 ? $(".remove_button, .mark_read_button").attr("disabled", !1) : $(".remove_button, .mark_read_button").attr("disabled", !0)
        });
        var t = $("#tracker_feed_form");
        $(".remove_button").on("click", function (e) {
            return e.preventDefault(), $(t).ajaxSubmit({
                form: $(t),
                url: "/json/tracker/feed/remove/",
                beforeSubmit: n.ajaxFormBeforSubmit,
                error: n.ajaxFormError,
                success: n.ajaxFormSuccess(n.ajaxFormRedirect)
            }), !1
        }), $(".mark_read_button").on("click", function (e) {
            return e.preventDefault(), $(t).ajaxSubmit({
                form: $(t),
                url: "/json/tracker/feed/mark_as_read/",
                beforeSubmit: n.ajaxFormBeforSubmit,
                error: n.ajaxFormError,
                success: n.ajaxFormSuccess(n.ajaxFormRedirect)
            }), !1
        })
    })
}, function (t, e, o) {
    o(175), o(176), o(177), o(178)
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        $("#give_invite_button").click(function () {
            var t = $(this).data("login");
            return confirm(window.i18nMsg("CONVERSATION_INVITE_USER")) && $.post("/json/users/send_invite/", {login: t}, function (t) {
                "ok" == t.messages ? ($.jGrowl(window.i18nMsg("INVITE_GIVE_COUNTER", {invites_string: t.invites_string}), {theme: "notice"}), n.ajaxFormRedirect(t)) : n.showSystemError(t)
            }), !1
        }), $("#send_invite").click(function () {
            var t = $(this).data("user_id");
            return $.post("/json/users/set_invite/", {user_id: t}, function (t) {
                "ok" == t.messages ? $.jGrowl(window.i18nMsg("INVITE_WAS_SENT"), {theme: "notice"}) : n.showSystemError(t)
            }, "json"), !1
        }), $("#go_ppg").click(function () {
            var t = $(this).data("user_id");
            return $.post("/json/users/ppg_comments/", {user_id: t}, function (t) {
                "ok" == t.messages ? $.jGrowl(window.i18nMsg("USER_SENT_PPG"), {theme: "notice"}) : n.showSystemError(t)
            }, "json"), !1
        }), $("#go_pis").click(function () {
            var t = $(this).data("user_id");
            return $.post("/json/users/ppg_smiles/", {user_id: t}, function (t) {
                "ok" == t.messages ? $.jGrowl(window.i18nMsg("USER_SENT_PPG"), {theme: "notice"}) : n.showSystemError(t)
            }, "json"), !1
        })
    })
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        var t = function () {
            var t = $(".js-userNoteForm"), e = $(".js-userNoteText");

            function o() {
                return $("#userNoteFormTextarea").val()
            }

            return {
                showForm: function () {
                    t.removeClass("hidden"), $("#userNoteFormTextarea").focus(), "" != o() && $(".js-saveUserNote").attr("disabled", !1)
                }, saveForm: function (o) {
                    var r, s = o.note;
                    "ok" == o.messages ? (r = "" != s ? window.i18nMsg("NOTICE_SAVED") : window.i18nMsg("NOTICE_DELETED"), $.jGrowl(r, {theme: "notice"}), "" != s ? (e.html(o.note), $(".js-userNote").removeClass("hidden"), t.addClass("hidden")) : ($(".js-noteBlockHeader").addClass("hidden"), $(".js-writeNoteBtn").removeClass("hidden"), t.addClass("hidden"))) : n.show_system_error(o)
                }, clearForm: function () {
                    e.val()
                }, getValue: o
            }
        }();
        $(".js-writeNoteBtn").on("click", function (e) {
            e.preventDefault(), $(this).addClass("hidden"), $(".js-noteBlockHeader").removeClass("hidden"), t.showForm()
        }), $(".js-editNoteBtn").on("click", function (e) {
            e.preventDefault(), $(".js-userNote").addClass("hidden"), $(".js-cancelNoteBtn").addClass("hidden"), $(".js-userNoteForm").addClass("edit-mode"), t.showForm()
        }), $(".js-cancelNoteBtn").on("click", function (t) {
            t.preventDefault(), $(".js-writeNoteBtn").removeClass("hidden"), $(".js-noteBlockHeader").addClass("hidden"), $(".js-userNoteForm").addClass("hidden"), $("#userNoteFormTextarea").val("").blur()
        }), $(".js-userNoteForm").on("change keyup", function () {
            var e = $(".js-saveUserNote");
            e.attr("disabled", !0), ("" != t.getValue() || $(".js-userNoteForm").hasClass("edit-mode")) && e.attr("disabled", !1)
        }), $(".js-userNoteForm").ajaxForm({
            dataType: "json", url: "/json/users/save_note/", beforeSubmit: function () {
                t.clearForm()
            }, success: function (e) {
                t.saveForm(e)
            }
        }), $(".js-showmore_items").on("click", function (t) {
            t.preventDefault();
            $(this).data("login");
            var e = $(this).data("type");
            $("#" + e + "_items li.hidden").removeClass("hidden"), $(this).remove()
        }), $("*[rel=hub-popover]").each(function () {
            var t = $(this);
            t.webuiPopover({
                animation: "fade",
                arrow: !1,
                padding: !1,
                placement: "bottom-right",
                trigger: "hover",
                offsetTop: 5,
                delay: {show: 1e3},
                type: "async",
                url: "/json/hubs/info/",
                async: {method: "POST", data: {alias: t.data("hub-alias")}},
                content: function (t) {
                    return t.html
                }
            })
        })
    })
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        $("#invite_form").ajaxForm({
            dataType: "json",
            url: "/json/users/give_invite_byform/",
            beforeSubmit: function () {
                $('#invite_form input[type="submit"]').addClass("loading")
            },
            success: function (t) {
                "ok" == t.messages ? ($.jGrowl(window.i18nMsg("INVITE_WAS_SENT"), {theme: "notice"}), $("#count_invite").text(t.invite_string), $(".used_invites").prepend('<div class="invite_item" id="invite_item_' + t.invite_id + '">                <img src="/register/icode/' + t.invite_code + '/"  alt="" />                <a href="mailto:' + t.to_email + '">' + t.to_email + '</a>                <a href="#cancel" class="cancel" title="' + window.i18nMsg("REVOKE_INVITE") + '" data-id="' + t.invite_id + '">x</a>            </div>'), $("#invite_form").resetForm()) : n.showSystemError(t), $('#invite_form input[type="submit"]').removeClass("loading")
            }
        }), $("img.invite_preloader").on("click", function () {
            return $(this).attr("src", $(this).data("invite_url")).removeClass("invite_preloader").attr("title", ""), !1
        }), $(".resend_invite").on("click", function () {
            var t = $(this), e = t.data("code");
            return $.post("/json/users/send_invite_notification/", {code: e}, function (e) {
                "ok" == e.messages ? t.replaceWith(window.i18nMsg("INVITE_WAS_SENT_AGAIN")) : n.showSystemError(e)
            }), !1
        }), $(".used_invites .invite_item .cancel").on("click", function () {
            var t = $(this).data("id");
            return $.post("/json/users/unset_invites/", {invite_id: t}, function (e) {
                "ok" == e.messages ? ($.jGrowl(window.i18nMsg("INVITE_WAS_REVOKED"), {theme: "notice"}), $("#invite_item_" + t).remove(), $("#count_invite").text(e.invite_string)) : n.showSystemError(e)
            }, "json"), !1
        })
    })
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        $("#karma_reset_input").click(function () {
            var t = !$(this).prop("checked");
            $('#karma_reset_form input[type="submit"]').attr("disabled", t)
        }), $("#karma_reset_form").ajaxForm({
            dataType: "json",
            url: "/json/users/reset/request/",
            beforeSubmit: function () {
                $('#karma_reset_form input[type="submit"]').addClass("loading")
            },
            success: function (t) {
                "ok" == t.messages ? $("#karma_reset").removeClass("karma_reseting").addClass("karma_noreseting") : n.showSystemError(t), $('#karma_reset_form input[type="submit"]').removeClass("loading")
            }
        }), $("#karma_reset_abort_form").ajaxForm({
            dataType: "json",
            url: "/json/users/reset/abort/",
            beforeSubmit: function () {
                $('#karma_reset_abort_form input[type="submit"]').addClass("loading")
            },
            success: function (t) {
                "ok" == t.messages ? $("#karma_reset").addClass("karma_reseting").removeClass("karma_noreseting") : n.showSystemError(t), $('#karma_reset_abort_form input[type="submit"]').removeClass("loading")
            }
        })
    })
}, function (t, e, o) {
    var n = o(2);
    $(document).ready(function () {
        "use strict";
        $("#rules_agreement").on("change", function () {
            var t = $(this).is(":checked");
            $(".js-welcome-btn").attr("disabled", !t)
        }), $("#welcome_form").ajaxForm({
            form: $("#welcome_form"),
            url: "/json/info/welcome/",
            beforeSubmit: n.ajaxFormBeforSubmit,
            error: n.ajaxFormError,
            success: n.ajaxFormSuccess(function (t, e, o, n) {
                var r = $("#notice_of_news").is(":checked"), s = $("#email_digest_weekly").is(":checked"),
                    a = "/" + window.g_current_hl + "/onboarding_finished/", i = "email_no";
                (r || s) && (i = `email${r ? "_news" : ""}${s ? "_digest" : ""}`), "function" == typeof ga && (ga("send", "pageview", a), ga("HGM.send", "pageview", a), ga("send", "event", "onboarding", "save", i))
            })
        })
    })
}, function (t, e) {
    $(document).ready(function () {
        var t = {
            pinToggleTitle: window.pinPostTitle,
            pinId: $("[data-pin-id]").data("pin-id"),
            pinTitleEl: $("[data-pin-title]"),
            pinTitle: $("[data-pin-title]").data("pin-title"),
            pinTitleUrl: $("[data-pin-title]").data("pin-title-url"),
            pinIconEl: $("[data-pin-icon]"),
            pinIconPlusEl: $("[data-pin-icon-plus]"),
            pinIconMinusEl: $("[data-pin-icon-minus]"),
            pinPostEl: $("[data-pin-post]"),
            restoreState: function () {
                var t = JSON.parse(localStorage.getItem("habr-pin-post"));
                if (t) {
                    var e = new Date;
                    t.value === this.pinId ? this.toggleVisiblePin() : localStorage.removeItem("habr-pin-post"), e.getMonth() - new Date(t.time).getMonth() > 3 && (this.toggleVisiblePin(), localStorage.removeItem("habr-pin-post"))
                }
            },
            toggleVisiblePin: function () {
                $(".pinned-block").show();
                var t = {value: this.pinId, time: new Date};
                this.pinPostEl.is(":visible") ? (this.pinPostEl.hide(), localStorage.setItem("habr-pin-post", JSON.stringify(t)), this.pinTitleEl.html('<a href="' + this.pinTitleUrl + '">' + this.pinTitle + "</a>"), this.pinIconPlusEl.show(), this.pinIconMinusEl.hide()) : (this.pinPostEl.css("display", "flex"), localStorage.removeItem("habr-pin-post"), this.pinTitleEl.html(this.pinToggleTitle), this.pinIconPlusEl.hide(), this.pinIconMinusEl.show())
            }
        };
        $(".pinned-block").length && t.restoreState(), $(".pinned-block").show(), t.pinIconEl.on("click", function () {
            t.toggleVisiblePin()
        })
    })
}, function (t, e) {
    t.exports = function (t) {
        if (t.children().eq(0).length) {
            var e, o, n, r, s = (e = function (t) {
                var e = $(window).scrollTop() - t.offset().top;
                t.children().eq(0).toggleClass("sticked", e >= 20)
            }.bind(null, t), o = 50, function () {
                var t = arguments, s = (new Date).getTime();
                n && s - n > o ? (n = s, e.apply(null, t)) : (clearTimeout(r), r = setTimeout(function () {
                    n = s, e.apply(null, t)
                }, o))
            });
            s(), $(window).on("scroll", s)
        }
    }
}, function (t, e) {
    t.exports = function (t, e) {
        $(t).find("h2[id], h3[id], li[id], p[id]").each(function (t, e) {
            var o = e.getAttribute("id");
            $(e).append('<a class="h-anchor" href="#{href}">¶</a>'.replace("{href}", o))
        })
    }
}, function (t, e) {
    t.exports = function (t) {
        if ($.browser.isMobileDevice = /android|webos|iphone|ipad|ipod|blackberry/i.test(navigator.userAgent.toLowerCase()), !($.browser.isMobileDevice || $(window).width() < 1024 || $(window).height() < 500 || screen.width < 1024)) {
            if ($(".content_left").outerHeight() < $(".sidebar_right").outerHeight()) return !1;
            var e = $(t);
            if (e.size()) {
                var o = e.width(), n = $(".sidebar_right");
                if (n.size()) {
                    var r = !1, s = $(".content_left"), a = function () {
                        var t;
                        t = 0 == r ? n.offset().top + n.outerHeight() : n.offset().top + n.outerHeight() + e.outerHeight() + 20, $(window).width() < 1024 || $(window).height() < 500 || screen.width < 1024 || (e.outerHeight() + this.pageYOffset > s.offset().top + s.outerHeight() ? (!0 === r && e.removeClass("float_block").css("width", "auto"), r = !1) : this.pageYOffset > t ? (!1 === r && (e.addClass("float_block").css("width", o + "px"), $(window).height() > e.outerHeight(!0) ? e.animate({opacity: 0}, 0, function () {
                            e.animate({opacity: 1}, 500)
                        }) : e.removeClass("float_block").css("width", "auto")), r = !0) : (!0 === r && e.removeClass("float_block").css("width", "auto"), r = !1))
                    };
                    $(window).bind("scroll resize", a), a()
                }
            }
        }
    }
}, , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , , function (t, e) {
}, , , function (t, e) {
}, function (t, e) {
}]);