// @ts-check

/**
 @typedef {`v` | `d` | `i` | `w` | `e`} LogPriority
 @typedef {{ (...data: any[]): void; (message?: any, ...optionalParams: any[]): void; }} LogMethod
 @typedef {(...args: any[]) => void} BindedLog
 @typedef {{ (priority: LogPriority, ...data: any[]): void; bindPriority(priority: LogPriority): BindedLog; }} BindedTagLog
 @typedef {{ (tag: string, ...data: any[]): void; bindTag(tag: string): BindedLog; }} BindedPriorityLog
 @typedef {{ VERBOSE: `v`; DEBUG: `d`; INFO: `i`; WARN: `w`; ERROR: `e`; }} LogPriorityConstants
 @typedef {{ (this: any, priority: LogPriority, tag: string, ...data: any[]): void; bindTag(this: any, tag: string): BindedTagLog; bindPriority(this: any, priority: LogPriority): BindedPriorityLog; } & LogPriorityConstants} Log
 */

const log = (function () {
    /** @type {{ [priority in LogPriority]: LogMethod }} */
    const LogMethods = {
        v: console.log, d: console.debug,
        i: console.info, w: console.warn, e: console.error
    };

    /**
     @param {LogMethod} method
     @param {string} tag

     @return {void}
     */
    function _log(method, /** @type {string} */ tag, /** ...data: any[] */) {
        const error = new Error();
        let stack = error.stack;
        /** @type {string | undefined} */
        let methodName = undefined;
        if (stack) {
            if (!methodName) {
                let count = 3;
                for (const match of stack.matchAll(/^\s*at\s+(.*)\s*\(/gm)) {
                    if (--count === 0) {
                        methodName = match[1];
                        break;
                    }
                }
            }
            if (!methodName) {
                let count = 3;
                for (const match of stack.matchAll(/^\s*(.*)\s*@/gm)) {
                    if (--count === 0) {
                        methodName = match[1];
                        break;
                    }
                }
            }
        }
        method(`[${tag}]${methodName ? `#${methodName}` : ``}${arguments.length > 2 ? `: ${arguments[2]}` : ``}`, ...Array.from(arguments).slice(3));
    }

    /** @type {Log} */
    const log = function (priority, tag) {
        _log.call(this, LogMethods[priority], ...Array.from(arguments).slice(1));
    }

    log.bindTag = function (tag) {
        /** @type {BindedTagLog} */
        const log = function (priority) {
            _log.call(this, LogMethods[priority], Array.from(arguments).slice(1));
        };
        log.bindPriority = function (priority) {
            return log.bind(this, priority);
        }
        return log;
    }

    log.bindPriority = function (priority) {
        /** @type {BindedPriorityLog} */
        const log = function (tag) {
            _log.call(this, LogMethods[priority], ...Array.from(arguments));
        }
        log.bindTag = function (tag) {
            return log.bind(this, tag);
        }
        return log;
    }

    log.VERBOSE = `v`;
    log.DEBUG = `d`;
    log.INFO = `i`;
    log.WARN = `w`;
    log.ERROR = `e`;
    log[Symbol.toStringTag] = function () {
        return "clients.js.utils.log.js:Log";
    }
    return log;
})()