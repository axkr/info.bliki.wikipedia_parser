local export = {}

-- Implements [[Template:lang]]
function export.lang_t(frame)
    local args = frame:getParent().args
    NAMESPACE = mw.title.getCurrentTitle().nsText

    local lang = args[1]; if lang == "" then lang = nil end
    local text = args[2] or ""
    local sc = args["sc"]; if sc == "" then sc = nil end
    local face = args["face"]; if face == "" then face = nil end

    lang = lang or (NAMESPACE == "Template" and "und") or error("Language code has not been specified. Please pass parameter 1 to the template.")
    lang = require("Module:languages").getByCode(lang) or error("The language code \"" .. lang .. "\" is not valid.")
    sc = (sc and (require("Module:scripts").getByCode(sc) or error("The script code \"" .. sc .. "\" is not valid.")) or nil)

    if not sc then
        sc = require("Module:scripts").findBestScript(text, lang)
    end

    return export.tag_text(text, lang, sc, face)
end

-- Wrap text in the appropriate HTML tags with language and script class.
function export.tag_text(text, lang, sc, face)
    -- Add a script wrapper
    if face == "term" then
        return '<i class="' .. sc:getCode() .. ' mention" lang="' .. lang:getCode() .. '">' .. text .. '</i>'
    elseif face == "head" then
        return '<strong class="' .. sc:getCode() .. ' headword" lang="' .. lang:getCode() .. '">' .. text .. '</strong>'
    elseif face == "bold" then
        return '<b class="' .. sc:getCode() .. '" lang="' .. lang:getCode() .. '">' .. text .. '</b>'
    elseif face == nil then
        return '<span class="' .. sc:getCode() .. '" lang="' .. lang:getCode() .. '">' .. text .. '</span>'
    else
        error("Invalid script face \"" .. face .. "\".")
    end
end

-- Add a notice to request the native script of a word
function export.request_script(lang, sc)
    local scripts = lang:getScripts()

    -- By default, request for "native" script
    local cat_script = "native"
    local disp_script = "script"

    -- If the script was not specified, and the language has only one script, use that.
    if not sc and #scripts == 1 then
        sc = scripts[1]
    end

    -- Is the script known?
    if sc then
        -- If the script is Latin, return nothing.
        if is_Latin_script(sc) then
            return ""
        end

        if sc:getCode() ~= scripts[1]:getCode() then
            disp_script = sc:getCanonicalName()
        end

        -- The category needs to be specific to script only if there is chance
        -- of ambiguity. This occurs when lang=und, or when the language has
        -- multiple scripts.
        if lang:getCode() == "und" or scripts[2] then
            cat_script = sc:getCanonicalName()
        end
    else
        -- The script is not known.
        -- Does the language have at least one non-Latin script in its list?
        local has_nonlatin = false

        for i, val in ipairs(scripts) do
            if not is_Latin_script(val) then
                has_nonlatin = true
                break
            end
        end

        -- If there are non-Latin scripts, return nothing.
        if not has_nonlatin then
            return ""
        end
    end

    local category = ""

    if mw.title.getCurrentTitle().nsText ~= "Template" then
        category = "[[Category:" .. lang:getCanonicalName() .. " terms needing " .. cat_script .. " script]]"
    end

    return "<small>[" .. disp_script .. "?]</small>" .. category
end

function export.template_rfscript(frame)
    local args = frame.args
    local lang = args[1] or error("The first parameter (language code) has not been given")
    local sc = args["sc"]; if sc == "" then sc = nil end
    lang = require("Module:languages").getByCode(lang) or error("The language code \"" .. lang .. "\" is not valid.")
    sc = (sc and (require("Module:scripts").getByCode(sc) or error("The script code \"" .. sc .. "\" is not valid.")) or nil)

    local ret = export.request_script(lang, sc)

    if ret == "" then
        error("This language is written in the Latin alphabet. It does not need a native script.")
    else
        return ret
    end
end

function is_Latin_script(sc)
    return (sc:getCode():find("Latn", nil, true)) or sc:getCode() == "Latinx"
end

return export
