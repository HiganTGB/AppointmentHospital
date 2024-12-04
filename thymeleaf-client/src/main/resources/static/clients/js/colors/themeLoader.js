// @ts-check
// import log from "../utils/log.js"
// import themes from "./themeRegistry"

/**
 @typedef {{ theme(name?: string): string; }} MaterialColorThemeLoader
 */

/** @type {MaterialColorThemeLoader} */
const loader = (function () {
    const _tagName = "clients.js.colors.themeLoader.js:MaterialColorThemeLoader"

    const _themeLoader = document.createElement("style");
    _themeLoader.id = "themeLoader";
    document.head.appendChild(_themeLoader);

    /**
     @param {string} [name]

     @return {string}
     */
    function theme(name) {
        log(log.VERBOSE, _tagName)
        const key = _tagName + ".dynamicThemeName";
        let old = localStorage.getItem(key);
        if (!old || !themes[old]) {
            old = Object.keys(themes)[0];
            if (!name) localStorage.setItem(key, old);
        }
        if (!name) {
            const oldTheme = themes[old];
            if (oldTheme) {
                let content = ``;
                for (const theme of oldTheme.stylesheet.cssRules)
                    content += theme.cssText;
                _themeLoader.innerHTML = content;
            }
            return old;
        }
        const newTheme = themes[name];
        if (newTheme) {
            let content = ``;
            for (const theme of newTheme.stylesheet.cssRules)
                content += theme.cssText;
            _themeLoader.innerHTML = content;
            newTheme.stylesheet.disabled = false;
            localStorage.setItem(key, name);
        }
        return old;
    }

    /**
     @this {Window}
     @param {Event} e

     @return {any}
     */
    function onLoadTheme(e) {
        this.document.adoptedStyleSheets = [
            ...this.document.adoptedStyleSheets,
            ...Object.values(themes).map(function (value) {
                return value.stylesheet;
            })
        ]
        theme();
    }

    /**
     @this {Window}
     @param {Event} e

     @return {any}
     */
    function onDispose(e) {
        window.removeEventListener("load", onLoadTheme);
        window.removeEventListener("unload", onDispose)
    }

    window.addEventListener("load", onLoadTheme);
    window.addEventListener("unload", onDispose)

    return /** @type {MaterialColorThemeLoader} */({
        theme, [Symbol.toStringTag]() {
            return _tagName;
        }
    });
})();