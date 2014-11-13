local export = {}

local Script = {}


function Script:getCode()
    return self._code
end


function Script:getCanonicalName()
    return self._rawData.names[1]
end


function Script:getOtherNames()
    if not self._otherNames then
        self._otherNames = {}

        for i, val in ipairs(self._rawData.names) do
            if i > 1 then
                table.insert(self._otherNames, val)
            end
        end
    end

    return self._otherNames
end


function Script:getAllNames()
    return self._rawData.names
end


function Script:getType()
    return "script"
end


function Script:getCategoryName()
    local name = self._rawData.names[1]

    -- If the name already has "script" in it, don't add it.
    if name:find("[Ss]script$") then
        return name
    else
        return name .. " script"
    end
end


function Script:countCharacters(text)
    if not self._rawData.characters then
        return 0
    else
        local _, num = mw.ustring.gsub(text, "[" .. self._rawData.characters .. "]", "")
        return num
    end
end


function Script:getRawData()
    return self._rawData
end


function Script:toJSON()
    local ret = {
        canonicalName = self:getCanonicalName(),
        categoryName = self:getCategoryName(),
        code = self._code,
        otherNames = self:getOtherNames(),
        type = self:getType(),
    }

    return require("Module:JSON").toJSON(ret)
end


Script.__index = Script


function export.makeObject(code, data)
    return data and setmetatable({ _rawData = data, _code = code }, Script) or nil
end


function export.getByCode(code)
    if code == "IPAchar" then
        require("Module:debug").track("IPAchar")
    end
    return export.makeObject(code, mw.loadData("Module:scripts/data")[code])
end


-- Find the best script to use, based on the characters of a string.
function export.findBestScript(text, lang)
    if not text or not lang then
        return export.getByCode("None")
    end

    local scripts = lang:getScripts()

    -- Try to match every script against the text,
    -- and return the one with the most matching characters.
    local bestcount = 0
    local bestscript = nil

    for i, script in ipairs(scripts) do
        local count = script:countCharacters(text)

        if count > bestcount then
            bestcount = count
            bestscript = script
        end
    end

    if bestscript then
        return bestscript
    end

    -- No matching script was found. Return "None".
    return export.getByCode("None")
end

return export


