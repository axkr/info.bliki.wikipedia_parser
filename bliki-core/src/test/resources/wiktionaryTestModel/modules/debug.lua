local export = {}

-- Convert a value to a string
function export.dump(value)
    local t = type(value)

    if t == "string" then
        return '"' .. value .. '"'
    elseif t == "table" then
        local str_table = {}

        for key, val in pairs(value) do
            table.insert(str_table, "[" .. export.dump(key) .. "] = " .. export.dump(val))
        end

        return "{" .. table.concat(str_table, ", ") .. "}"
    else
        return tostring(value)
    end
end

function export.track(key)
    local frame = mw.getCurrentFrame()
    pcall(frame.expandTemplate, frame, { title = 'tracking/' .. key })
end

-- Trigger a script error from a template
function export.error(frame)
    error(frame.args[1] or "(no message specified)")
end

return export
