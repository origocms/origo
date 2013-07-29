$(function () {
    var createLink = {
        /**
         * Turns selection into a link
         * If selection is already a link, it removes the link and wraps it with a <code> element
         * The <code> element is needed to avoid auto linking
         *
         * @example
         *    // either ...
         *    wysihtml5.commands.createLink.exec(composer, "createLink", "http://www.google.de");
         *    // ... or ...
         *    wysihtml5.commands.createLink.exec(composer, "createLink", { href: "http://www.google.de", target: "_blank" });
         */
        exec: function (composer, command, value) {
            var anchors = this.state(composer, command);
            if (anchors) {
                // Selection contains links
                composer.selection.executeAndRestore(function () {
                    _removeFormat(composer, anchors);
                });
            } else {
                // Create links
                value = typeof(value) === "object" ? value : { href: value };
                _format(composer, value);
            }
        },

        state: function (composer, command) {
            return wysihtml5.commands.formatInline.state(composer, command, "A");
        }
    }
    $.extend($.fn.wysihtml5.Constructor.prototype, createLink);
});