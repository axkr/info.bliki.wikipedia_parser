local export = {}
function export.show(frame)
    return mw.getCurrentFrame():extensionTag{
        name = "normal", args = { "cde", 234, "vwx" }
    }
end
return export
