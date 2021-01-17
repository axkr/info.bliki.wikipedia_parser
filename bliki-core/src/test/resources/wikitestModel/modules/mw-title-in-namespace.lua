local export = {}

function export.inNamespaceTest(frame)
    local ns = frame.args[1]
    local title = mw.title.getCurrentTitle()
    local result = title:inNamespace(ns)
    local msg = "inNamespace(" .. ns .. ") with title = " .. tostring(title) .. " returns " .. tostring(result) .. "."
    return msg
end

return export