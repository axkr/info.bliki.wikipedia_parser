local export = {}

-- Format the categories with the appropriate sort key
function export.format_categories(categories, lang, sort_key, sort_base)
    NAMESPACE = NAMESPACE or mw.title.getCurrentTitle().nsText

    if NAMESPACE == "" or NAMESPACE == "Appendix" then
        PAGENAME = PAGENAME or mw.title.getCurrentTitle().text
        SUBPAGENAME = SUBPAGENAME or mw.title.getCurrentTitle().subpageText

        if not lang then
            lang = require("Module:languages").getByCode("und")
        end

        -- Generate a default sort key
        sort_base = lang:makeSortKey(sort_base or SUBPAGENAME)

        if sort_key then
            -- Gather some statistics regarding sort keys
            if mw.ustring.lower(sort_key) == sort_base then
                table.insert(categories, "Sort key tracking/redundant")
            elseif lang:getCode() ~= "und" and lang:getCode() ~= "cmn" and lang:getCode() ~= "ja" and lang:getCode() ~= "zu" and lang:getCode() ~= "nan" and lang:getCode() ~= "yue" and lang:getCode() ~= "ko" then
                if lang:getCode() == "ga" or lang:getCode() == "gv" or lang:getCode() == "nv" or lang:getCode() == "roa-jer" or lang:getCode() == "fr" or lang:getCode() == "rm" or lang:getCode() == "prg" or lang:getCode() == "gd" or lang:getCode() == "twf" or lang:getCode() == "en" or lang:getCode() == "ro" or lang:getCode() == "egl" or lang:getCode() == "roa-tar" or lang:getCode() == "gl" or lang:getCode() == "ast" or lang:getCode() == "br" then
                    table.insert(categories, "Sort key tracking/needed/" .. lang:getCode())
                else
                    table.insert(categories, "Sort key tracking/needed")
                end
            end
        else
            sort_key = sort_base
        end

        -- If the resulting key is the same as the wiki software's default, remove it
        if sort_key == PAGENAME then
            sort_key = nil
        end

        for key, cat in ipairs(categories) do
            categories[key] = "[[Category:" .. cat .. (sort_key and "|" .. sort_key or "") .. "]]"
        end

        return table.concat(categories, "")
    else
        return ""
    end
end

-- Used by {{categorize}}
function export.template_categorize(frame)
    NAMESPACE = NAMESPACE or mw.title.getCurrentTitle().nsText
    local format = frame.args["format"]
    local args = frame:getParent().args

    local langcode = args[1]; if langcode == "" then langcode = nil end
    local sort_key = args["sort"]; if sort_key == "" then sort_key = nil end
    local categories = {}

    if not langcode then
        if NAMESPACE == "Template" then return "" end
        error("Language code has not been specified. Please pass parameter 1 to the template.")
    end

    local lang = require("Module:languages").getByCode(langcode)

    if not lang then
        if NAMESPACE == "Template" then return "" end
        error("The language code \"" .. langcode .. "\" is not valid.")
    end

    local prefix = ""

    if format == "pos" then
        prefix = lang:getCanonicalName() .. " "
    elseif format == "topic" then
        prefix = lang:getCode() .. ":"
    end

    local i = 2
    local cat = args[i]

    while cat do
        if cat ~= "" then
            table.insert(categories, prefix .. cat)
        end

        i = i + 1
        cat = args[i]
    end

    return export.format_categories(categories, lang, sort_key)
end

return export
