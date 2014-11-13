local export = {}

local Language = {}


function Language:getCode()
    return self._code
end


function Language:getCanonicalName()
    return self._rawData.names[1]
end


function Language:getAllNames()
    return self._rawData.names
end


function Language:getOtherNames()
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


function Language:getType()
    return self._rawData.type
end


function Language:getWikimediaLanguages()
    if not self._wikimediaLanguageObjects then
        local m_wikimedia_languages = require("Module:wikimedia languages")
        self._wikimediaLanguageObjects = {}
        local wikimedia_codes = self._rawData.wikimedia_codes or {self._code}

        for _, wlangcode in ipairs(wikimedia_codes) do
            table.insert(self._wikimediaLanguageObjects, m_wikimedia_languages.getByCode(wlangcode))
        end
    end

    return self._wikimediaLanguageObjects
end


function Language:getScripts()
    if not self._scriptObjects then
        local m_scripts = require("Module:scripts")
        self._scriptObjects = {}

        for _, sc in ipairs(self._rawData.scripts) do
            table.insert(self._scriptObjects, m_scripts.getByCode(sc))
        end
    end

    return self._scriptObjects
end


function Language:getFamily()
    if not self._familyObject then
        self._familyObject = require("Module:families").getByCode(self._rawData.family)
    end

    return self._familyObject
end


function Language:getAncestors()
    if not self._ancestorObjects then
        self._ancestorObjects = {}

        for _, ancestor in ipairs(self._rawData.ancestors or {}) do
            table.insert(self._ancestorObjects, export.getByCode(ancestor))
        end
    end

    return self._ancestorObjects
end


function Language:getAncestorChain()
    if not self._ancestorChain then
        self._ancestorChain = {}
        local step = #self:getAncestors() == 1 and self:getAncestors()[1] or nil

        while step do
            table.insert(self._ancestorChain, 1, step)
            step = #step:getAncestors() == 1 and step:getAncestors()[1] or nil
        end
    end

    return self._ancestorChain
end


function Language:getCategoryName()
    local name = self._rawData.names[1]

    -- If the name already has "language" in it, don't add it.
    if name:find("[Ll]anguage$") then
        return name
    else
        return name .. " language"
    end
end


function Language:makeEntryName(text)
    text = mw.ustring.gsub(text, "^[¿¡]", "")
    text = mw.ustring.gsub(text, "[؟?!;՛՜ ՞ ՟？！।॥။၊་།]$", "")

    if self._rawData.entry_name then
        for i, from in ipairs(self._rawData.entry_name.from) do
            local to = self._rawData.entry_name.to[i] or ""
            text = mw.ustring.gsub(text, from, to)
        end
    end

    return text
end


function Language:makeSortKey(name)
    name = mw.ustring.lower(name)

    -- Remove initial hyphens and *
    name = mw.ustring.gsub(name, "^[-־ـ*]+(.)",
        "%1")
    -- Remove anything in parentheses, as long as they are either preceded or followed by something
    name = mw.ustring.gsub(name, "(.)%([^()]+%)", "%1")
    name = mw.ustring.gsub(name, "%([^()]+%)(.)", "%1")

    -- If there are language-specific rules to generate the key, use those
    if self._rawData.sort_key then
        for i, from in ipairs(self._rawData.sort_key.from) do
            local to = self._rawData.sort_key.to[i] or ""
            name = mw.ustring.gsub(name, from, to)
        end
    end

    return mw.ustring.upper(name)
end


function Language:transliterate(text, sc, module_override)
    if not ((module_override or self._rawData.translit_module) and text) then
        return nil
    end

    if module_override then
        require("Module:debug").track("module_override")
    end

    return require("Module:" .. (module_override or self._rawData.translit_module)).tr(text, self:getCode(), sc and sc:getCode() or nil)
end


function Language:toJSON()
    local entryNamePatterns = nil

    if self._rawData.entry_name then
        entryNamePatterns = {}

        for i, from in ipairs(self._rawData.entry_name.from) do
            local to = self._rawData.entry_name.to[i] or ""
            table.insert(entryNamePatterns, {from = from, to = to})
        end
    end

    local ret = {
        ancestors = self._rawData.ancestors,
        canonicalName = self:getCanonicalName(),
        categoryName = self:getCategoryName(),
        code = self._code,
        entryNamePatterns = entryNamePatterns,
        family = self._rawData.family,
        otherNames = self:getOtherNames(),
        scripts = self._rawData.scripts,
        type = self:getType(),
        wikimediaLanguages = self._rawData.wikimedia_codes,
    }

    return require("Module:JSON").toJSON(ret)
end


-- Do NOT use this method!
-- All uses should be pre-approved on the talk page!
function Language:getRawData()
    return self._rawData
end

Language.__index = Language


local function getDataModuleName(code)
    if code:find("^[a-z][a-z]$") then
        return "languages/data2"
    elseif code:find("^[a-z][a-z][a-z]$") then
        local prefix = code:sub(1, 1)
        return "languages/data3/" .. prefix
    elseif code:find("^[a-z-]+$") then
        return "languages/datax"
    else
        return nil
    end
end


local function getRawLanguageData(code)
    local modulename = getDataModuleName(code)
    return modulename and mw.loadData("Module:" .. modulename)[code] or nil
end


function export.makeObject(code, data)
    return data and setmetatable({ _rawData = data, _code = code }, Language) or nil
end


function export.getByCode(code)
    return export.makeObject(code, getRawLanguageData(code))
end


function export.getByCanonicalName(name)
    local code = mw.loadData("Module:languages/by name")[name]

    if not code then
        return nil
    end

    return export.makeObject(code, getRawLanguageData(code))
end


function export.iterateAll()
    mw.incrementExpensiveFunctionCount()
    local m_data = mw.loadData("Module:languages/alldata")
    local func, t, var = pairs(m_data)

    return function()
        local code, data = func(t, var)
        return export.makeObject(code, data)
    end
end

return export
