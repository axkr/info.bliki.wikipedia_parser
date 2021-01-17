-- This file is simplification of the situation that calling normal listed arguments.
local export = {}
function export.show(frame)
    return require("Module:parser-function-caller")("listed-args", { 234, "cde", 345 })
end
return export
