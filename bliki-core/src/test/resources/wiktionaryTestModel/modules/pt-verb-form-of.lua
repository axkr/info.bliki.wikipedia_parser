local abbrevs = {
    ["i"]="personal infinitive ",
    ["g"]="gerund ",
    ["pp"]="past participle ",
    ["spp"]="short past participle ",
    ["lpp"]="long past participle ",
    ["psi"]="present indicative ",
    ["ipi"]="imperfect indicative ",
    ["pti"]="preterite indicative ",
    ["pli"]="pluperfect indicative ",
    ["fti"]="future indicative ",
    ["c"]="conditional ",
    ["pss"]="present subjunctive ",
    ["ips"]="imperfect subjunctive ",
    ["fts"]="future subjunctive ",
    ["ai"]="affirmative imperative ",
    ["ni"]="negative imperative ",

    ["1"]="first-person ",
    ["2"]="second-person ",
    ["3"]="third-person ",
    ["s"]="singular ",
    ["p"]="plural ",
    ["m"]="masculine ",
    ["f"]="feminine ",
    ["mf"]="masculine and feminine ",
    [""] = ""
}




-- Format: [suffix+conj] = {vf, p, n, which gloss, gender, context}
local suffixes = {}
suffixes = {
    ["a1"]={{"psi", "3", "s", 1}, {"ai", "2", "s", 2}},
    ["a2"]={{"pss", "1" ,"s", 1}, {"pss", "3", "s", 2}, {"ai", "3", "s", 1}, {"ni", "3", "s", 1}},
    ["a3"]={{"pss", "1" ,"s", 1}, {"pss", "3", "s", 2}, {"ai", "3", "s", 1}, {"ni", "3", "s", 1}},
    ["e1"]={{"pss", "1", "s", 1}, {"pss", "3", "s", 2}, {"ai", "3", "s", 1}, {"ni", "3", "s", 1}},
    ["e2"]={{"psi", "3", "s", 1}, {"ai", "2", "s", 2}},
    ["e3"]={{"psi", "3", "s", 1}, {"ai", "2", "s", 2}},
    ["i2"]={{"pti", "1", "s"}},
    ["i3"]={{"pti", "1", "s"}, {"ai", "2", "p"}},
    ["o1"]={{"psi", "1", "s"}},
    ["o2"]={{"psi", "1", "s"}},
    ["o3"]={{"psi", "1", "s"}},
    ["ai1"]={{"ai", "2", "p"}},
    ["am1"]={{"psi", "3", "p"}},
    ["am2"]={{"pss", "3", "p"}, {"ai", "3", "p"}, {"ni", "3", "p"}},
    ["am3"]={{"pss", "3", "p"}, {"ai", "3", "p"}, {"ni", "3", "p"}},
    ["ar1"]={{"i", "1", "s", 1}, {"i", "3", "s", 1}, {"fts", "1", "s", 1}, {"fts", "3", "s", 2}},
    ["as1"]={{"psi", "2", "s"}},
    ["as2"]={{"pss", "2", "s"}, {"ni", "2", "s"}},
    ["as3"]={{"pss", "2", "s"}, {"ni", "2", "s"}},
    ["ei1"]={{"pti", "1", "s"}},
    ["ei2"]={{"ai", "2", "p"}},
    ["em1"]={{"pss", "3", "p"}, {"ai", "3", "p"}, {"ni", "3", "p"}},
    ["em2"]={{"psi", "3", "p"}},
    ["em3"]={{"psi", "3", "p"}},
    ["er2"]={{"i", "1", "s", 1}, {"i", "3", "s", 1}, {"fts", "1", "s", 1}, {"fts", "3", "s", 2}},
    ["es1"]={{"pss", "2", "s"}, {"ni", "2", "s"}},
    ["es2"]={{"psi", "2", "s"}},
    ["es3"]={{"psi", "2", "s"}},
    ["eu2"]={{"pti", "3", "s"}},
    ["ia2"]={{"ipi", "1", "s"}, {"ipi", "3", "s"}},
    ["ia3"]={{"ipi", "1", "s"}, {"ipi", "3", "s"}},
    ["ir3"]={{"i", "1", "s", 1}, {"i", "3", "s", 1}, {"fts", "1", "s", 1}, {"fts", "3", "s", 2}},
    ["is3"]={{"psi", "2", "p"}},
    ["iu3"]={{"pti", "3", "s"}},
    ["ou1"]={{"pti", "3", "s"}},
    ["ada1"]={{"pp", "f", "s"}},
    ["ado1"]={{"pp", "m", "s"}},
    ["ais1"]={{"psi", "2", "p"}},
    ["ais2"]={{"pss", "2", "p"}, {"ni", "2", "p"}},
    ["ais3"]={{"pss", "2", "p"}, {"ni", "2", "p"}},
    ["ara1"]={{"pli", "1", "s"}, {"pli", "3", "s"}},
    ["ará1"]={{"fti", "3", "s"}},
    ["ava1"]={{"ipi", "1", "s"}, {"ipi", "3", "s"}},
    ["eis1"]={{"pss", "2", "p"}, {"ni", "2", "p"}},
    ["eis2"]={{"psi", "2", "p"}},
    ["era2"]={{"pli", "1", "s"}, {"pli", "3", "s"}},
    ["erá2"]={{"fti", "3", "s"}},
    ["iam2"]={{"ipi", "3", "p"}},
    ["iam3"]={{"ipi", "3", "p"}},
    ["ias2"]={{"ipi", "2", "s"}},
    ["ias3"]={{"ipi", "2", "s"}},
    ["ida2"]={{"pp", "f", "s"}},
    ["ida3"]={{"pp", "f", "s"}},
    ["ido2"]={{"pp", "m", "s"}},
    ["ido3"]={{"pp", "m", "s"}},
    ["ira3"]={{"pli", "1", "s"}, {"pli", "3", "s"}},
    ["irá3"]={{"fti", "3", "s"}},
    ["adas1"]={{"pp", "f", "p"}},
    ["ados1"]={{"pp", "m", "p"}},
    ["amos1"]={{"psi", "1", "p", 1}, {"pti", "1", "p", 2, "Brazil[[Category:Brazilian Portuguese verb forms]]"}},
    ["amos2"]={{"pss", "1", "p"}, {"ai", "1", "p"}, {"ni", "1", "p"}},
    ["amos3"]={{"pss", "1", "p"}, {"ai", "1", "p"}, {"ni", "1", "p"}},
    ["ámos1"]={{"pti", "1", "p", 1, "Portugal[[Category:European Portuguese verb forms]]"}},
    ["ando1"]={{"g", "", ""}},
    ["aram1"]={{"pti", "3", "p"}, {"pli", "3", "p"}},
    ["arão1"]={{"fti", "3", "p"}},
    ["aras1"]={{"pli", "2", "s"}},
    ["arás1"]={{"fti", "2", "s"}},
    ["arei1"]={{"fti", "1", "s"}},
    ["arem1"]={{"i", "3", "p", 1}, {"fts", "3", "p", 2}},
    ["ares1"]={{"i", "2", "s", 1}, {"fts", "2", "s", 2}},
    ["aria1"]={{"c", "1", "s"}, {"c", "3", "s"}},
    ["asse1"]={{"ips", "1", "s"}, {"ips", "3", "s"}},
    ["aste1"]={{"pti", "2", "s"}},
    ["avam1"]={{"ipi", "3", "p"}},
    ["avas1"]={{"ipi", "2", "s"}},
    ["emos1"]={{"pss", "1", "p"}, {"ai", "1", "p"}, {"ni", "1", "p"}},
    ["emos2"]={{"psi", "1", "p", 1}, {"pti", "1", "p", 2}},
    ["endo2"]={{"g", "", ""}},
    ["eram2"]={{"pti", "3", "p"}, {"pli", "3", "p"}},
    ["erão2"]={{"fti", "3", "p"}},
    ["eras2"]={{"pli", "2", "s"}},
    ["erás2"]={{"fti", "2", "s"}},
    ["erei2"]={{"fti", "1", "s"}},
    ["erem2"]={{"i", "3", "p", 1}, {"fts", "3", "p", 2}},
    ["eres2"]={{"i", "2", "s", 1}, {"fts", "2", "s", 2}},
    ["eria2"]={{"c", "1", "s"}, {"c", "3", "s"}},
    ["esse2"]={{"ips", "1", "s"}, {"ips", "3", "s"}},
    ["este2"]={{"pti", "2", "s"}},
    ["idas2"]={{"pp", "f", "p"}},
    ["idas3"]={{"pp", "f", "p"}},
    ["idos2"]={{"pp", "m", "p"}},
    ["idos3"]={{"pp", "m", "p"}},
    ["íeis2"]={{"ipi", "2", "p"}},
    ["íeis3"]={{"ipi", "2", "p"}},
    ["imos3"]={{"psi", "1", "p", 1}, {"pti", "1", "p", 2}},
    ["indo3"]={{"g", "", ""}},
    ["iram3"]={{"pti", "3", "p"}, {"pli", "3", "p"}},
    ["irão3"]={{"fti", "3", "p"}},
    ["iras3"]={{"pli", "2", "s"}},
    ["irás3"]={{"fti", "2", "s"}},
    ["irei3"]={{"fti", "1", "s"}},
    ["irem3"]={{"i", "3", "p", 1}, {"fts", "3", "p", 2}},
    ["ires3"]={{"i", "2", "s", 1}, {"fts", "2", "s", 2}},
    ["iria3"]={{"c", "1", "s"}, {"c", "3", "s"}},
    ["isse3"]={{"ips", "1", "s"}, {"ips", "3", "s"}},
    ["iste3"]={{"pti", "2", "s"}},
    ["ardes1"]={{"i", "2", "p", 1}, {"fts", "2", "p", 2}},
    ["areis1"]={{"fti", "2", "p"}},
    ["áreis1"]={{"pli", "2", "p"}},
    ["ariam1"]={{"c", "3", "p"}},
    ["arias1"]={{"c", "2", "s"}},
    ["armos1"]={{"i", "1", "p", 1}, {"fts", "1", "p", 2}},
    ["assem1"]={{"ips", "3", "p"}},
    ["asses1"]={{"ips", "2", "s"}},
    ["astes1"]={{"pti", "2", "p"}},
    ["áveis1"]={{"ipi", "2", "p"}},
    ["erdes2"]={{"i", "2", "p", 1}, {"fts", "2", "p", 2}},
    ["ereis2"]={{"fti", "2", "p"}},
    ["êreis2"]={{"pli", "2", "p"}},
    ["eriam2"]={{"c", "3", "p"}},
    ["erias2"]={{"c", "2", "s"}},
    ["ermos2"]={{"i", "1", "p", 1}, {"fts", "1", "p", 2}},
    ["essem2"]={{"ips", "3", "p"}},
    ["esses2"]={{"ips", "2", "s"}},
    ["estes2"]={{"pti", "2", "p"}},
    ["íamos2"]={{"ipi", "1", "p"}},
    ["íamos3"]={{"ipi", "1", "p"}},
    ["irdes3"]={{"i", "2", "p", 1}, {"fts", "2", "p", 2}},
    ["ireis3"]={{"fti", "2", "p"}},
    ["íreis3"]={{"pli", "2", "p"}},
    ["iriam3"]={{"c", "3", "p"}},
    ["irias3"]={{"c", "2", "s"}},
    ["irmos3"]={{"i", "1", "p", 1}, {"fts", "1", "p", 2}},
    ["issem3"]={{"ips", "3", "p"}},
    ["isses3"]={{"ips", "2", "s"}},
    ["istes3"]={{"pti", "2", "p"}},
    ["áramos1"]={{"pli", "1", "p"}},
    ["aremos1"]={{"fti", "1", "p"}},
    ["aríeis1"]={{"c", "2", "p"}},
    ["ásseis1"]={{"ips", "2", "p"}},
    ["ávamos1"]={{"ipi", "1", "p"}},
    ["êramos2"]={{"pli", "1", "p"}},
    ["eremos2"]={{"fti", "1", "p"}},
    ["eríeis2"]={{"c", "2", "p"}},
    ["êsseis2"]={{"ips", "2", "p"}},
    ["íramos3"]={{"pli", "1", "p"}},
    ["iremos3"]={{"fti", "1", "p"}},
    ["iríeis3"]={{"c", "2", "p"}},
    ["ísseis3"]={{"ips", "2", "p"}},
    ["aríamos1"]={{"c", "1", "p"}},
    ["ássemos1"]={{"ips", "1", "p"}},
    ["eríamos2"]={{"c", "1", "p"}},
    ["êssemos2"]={{"ips", "1", "p"}},
    ["iríamos3"]={{"c", "1", "p"}},
    ["íssemos3"]={{"ips", "1", "p"}},
}


local export = {}
local w

function export.show(frame)

    -- This template supports two types of parameter entering:
    --- by data: manually enter the tense/aspect form, the person, the number,
    ---          etc.
    --- by suffix: enter the conjugation paradigm (1, 2 or 3) and the suffix
    ---            expected from a regular verb (even if the verb itself isn’t
    ---            regular) and the template will automatically find out all the
    ---            forms that suffix forms.

    local args = frame:getParent().args
    w = mw.title.getCurrentTitle().text
    local l = args[1] -- lemma
	local vf = args[2] or ""
	local tg = args["tg"] or ""
	local tg2 = args["tg2"] or tg or ""
	local ig = args["ig"] or ""
	local ig2 = args["ig2"] or ig or ""
	local obj = args["obj"] or ""
	local objtr = args["objtr"] or ""
	local c = args["cx"] or ""
	local imp = false
	if (args["imp"]) then imp = true end
	local data
	local suf
	local conj
	local s = ""

	if (mw.ustring.sub(l, -2) == "ar") then conj = "1"
	elseif (mw.ustring.sub(l, -2) == "ir") then conj = "3"
	else conj = 2 end

	if (args[2] == nil) then
	    suf = get_suffix(l)
	    --print(suf)
    end

    print("conj:"..conj)

	if (args[3] == nil and args[2] ~= "g") then
	    suf = suf or args[2]

	    data = suffixes[suf .. conj]

	    local correct_tg, correct_ig

	    for c1 = 1, table.getn(data) do
	        if (data[c1][4] == 2) then
	            correct_tg = tg2
	            correct_ig = ig2
	        else
	            correct_tg = tg
	            correct_ig = ig
	        end

	        local context = c
	        if (c ~= "" and data[c1][5] ~= nil) then
	            context = context .. ", "
	        end
	        context = context .. (data[c1][5] or "")

	        if (c1 > 1) then s = s .. "\n# " end
	        s = s .. def(l, context, data[c1][1], data[c1][2], data[c1][3], correct_tg, correct_ig, imp, obj, objtr)
	    end
	else
	    local p = args[3] or ""
	    local n = args[4]

	    s = s .. def(l, c, args[2], p, n, tg, ig, imp, obj, objtr)

	end

	return s;

end

-- Returns the text for the definition and the usex. Parameters:
--- l:     lemma
--- c:     context (i.e. “Portugal” for “falámos”)
--- vf:    verb form (abbreviation)
--- p:     person (abbreviation) or gender (for past participle)
--- n:     number (abbreviation)
--- tg:    transitive sense gloss
--- ig:    intransitive sense gloss
--- imp:   impersonal
--- obj:   object
--- objtr: object translation
function def(l, c, vf, p, n, tg, ig, imp, obj, objtr)
    local s = ""
    if (mw.ustring.len(c) > 0) then s = s .. "(''" .. c .. "'') " end
    s = s .. "''" .. abbrevs[p] .. abbrevs[n]
    if (vf ~= "g" and vf ~= "pp" and vf ~= "spp" and vf ~= "lpp") then
        s = s .. "(" .. pronoun_notes(vf, p, n) .. ") "
    end
    s = s .. abbrevs[vf] .. "of "
    s = s .. "'''''[[" .. l .. "#Portuguese|" .. l .. "]]'''"
    s = s .. make_usex(vf, p, n, tg, true, imp, obj, objtr)
    s = s .. make_usex(vf, p, n, ig, false, imp, obj, objtr)
    return s
end



-- Returns information about the pronouns used with the form. Parameters:
--- vf:    verb form (abbreviation)
--- p:     person (abbreviation)
--- n:     number (abbreviation)
function pronoun_notes(vf, p, n)
    local s = "'''[["
    if (p == "3") then
        if (not(vf == "ni" or vf == "ai")) then
            if (n == "s") then
                s = s .. "ele#Portuguese|ele]]''' and '''[[ela#Portuguese|ela]]''', also used with '''[[você#Portuguese|você"
            else
                s = s .. "eles#Portuguese|eles]]''' and '''[[elas#Portuguese|elas]]''', also used with '''[[vocês#Portuguese|vocês"
            end
            s = s .. "]]''' and [[Appendix:Portuguese pronouns|others]]"
        else
         if (n == "s") then
                s = s .. "você]]'''"
            else
                s = s .. "vocês]]'''"
            end
        end


    else
        if (p == "2") then
            if (n == "s") then
                s = s .. "tu#Portuguese|tu"
            else
                s = s .. "vós#Portuguese|vós"
            end
        else
            if (n == "s") then
                s = s .. "eu#Portuguese|eu"
            else
                s = s .. "nós#Portuguese|nós"
            end
        end
        s = s .. "]]'''"
    end
    return s;
end


-- Returns the word’s inflectional suffix.
function get_suffix(l)
    print("suffix:" .. l)
    local suf4 = mw.ustring.sub(l, -4)
    local suf3 = mw.ustring.sub(l, -3)
    local wsuf

    print("suf4:" .. suf4)
    print("suf3:" .. suf3)

    if (suf4 == "guer" or suf4 == "guir" or suf4 == "quer" or suf4 == "quir") then
        wsuf = mw.ustring.sub(w, mw.ustring.len(l) - mw.ustring.len(w) - 3)
        if (mw.ustring.sub(wsuf, 1, 1) == "u") then
            wsuf = mw.ustring.sub(wsuf, 2)
        end
    else
        wsuf = mw.ustring.sub(w, mw.ustring.len(l) - mw.ustring.len(w) - 2)
        local fl = mw.ustring.sub(wsuf, 1, 1) -- first letter
        local ftl = mw.ustring.sub(wsuf, 1, 2) -- first two letters


        if (suf3 == "car" or suf3 == "gar") then
            if (mw.ustring.sub(wsuf, 1, 1) == "u") then
                wsuf = mw.ustring.sub(wsuf, 2)
            end

        elseif (suf3 == "air" or suf3 == "uir") then
            if (ftl == "io" or ftl == "ia") then
                wsuf = mw.ustring.sub(wsuf, 2)

            elseif (fl == "í") then
                if (suffixes[wsuf .. "3"] == nil) then
                    wsuf = "i" .. mw.ustring.sub(wsuf, 2)
                end
            elseif (wsuf == "i" or wsuf == "is") then
                wsuf = "e" .. mw.ustring.sub(wsuf, 2)
            end
        elseif (suf3 == "ear") then
            if (fl == "i") then
                wsuf = mw.ustring.sub(wsuf, 2)
            end
        elseif (suf3 == "oer") then
            if (fl == "i") then
                wsuf = "e" .. mw.ustring.sub(wsuf, 2)
            elseif (fl == "í" and mw.ustring.len(wsuf <=3)) then
                wsuf = "i" .. mw.ustring.sub(wsuf, 2)
            end
        end
    end
    return wsuf
end


-- Generates an automatic usage example. Returns an empty string if one can’t be
-- generated.

-- parameters:
--- vf:    verb form (abbreviation)
--- p:     person (abbreviation)
--- n:     number (abbreviation)
--- g:     gloss
--- tr:    whether the verb sense is transitive
--- imp:   (boolean) whether the verb is impersonal (i.e. chover, gear)
--- obj:   the object; default “isso”
--- objtr: the object’s translation; default “this”

-- The gloss should be the English form that translates the Portuguese one.
-- For example, the form of ‘to be’ to be used is:
--- be:    ai, ni, fti, c, pss, i
--- am:    psi 1s, fts 1s
--- is:    psi 3s, fts 3s
--- are    psi 2s p, fts 2s p
--- was:   pti 1s 3s, ipi 1s 3s
--- were:  pti 2s p, ipi 2s p, ips
--- been:  pp, lpp, spp, pli
--- being: g
function make_usex(vf, p, n, g, tr, imp, obj, objtr)
    if (g == "") then return "" end

    local pt = ""
    local en = ""
    local ww

    if (mw.ustring.len(obj) == 0) then
        obj = "isso"
        objtr = "this"
    end

    if (vf == "g") then
        if (imp == true) then
            pt = pt .. "Aqui está '''" .. w .. "'''"
            en = en .. "It is '''" .. g .. "''' here"
        else
            pt = pt .. "Conheço a pessoa '''" .. w .. "'''"
            en = en .. "I know the person '''" .. g .. "'''"
            if (tr) then
                pt = pt .. " " .. obj
                en = en .. " " .. objtr
            end
        end



    elseif (vf == "pp" or vf == "lpp" or vf == "spp") then
        if (p == "m" and n == "s") then
            if (imp == true) then
                pt = pt .. "Havia '''" .. w .. "'''"
                en = en .. "It had '''" .. g .. "'''"
            else
                pt = pt .. "Alguém havia '''" .. w .. "'''"
                en = en .. "Someone had '''" .. g .. "'''"
                if (tr == true) then
                    pt = pt .. " " .. obj
                    en = en .. " " .. objtr
                end
            end
        else return ""
        end


    else
        if (vf == "ni" or vf == "ai") then
            ww = w
            if (p == "1") then
                en = en .. "'''Let’s"
                if (vf == "ni") then
                    pt = pt .. "Não "
                    en = en .. "''' not '''"
                else
                    ww = capitalise(w)
                    en = en .. " "
                end
            else
                if (p == "2" and n == "s") then pt = pt .. "Tu"
                elseif (p == "2" and n == "p") then pt = pt .. "Vós"
                elseif (p == "3" and n == "s") then pt = pt .. "Você"
                else pt = pt .. "Vocês" end
                pt = pt .. " aí, "
                en = en .. "You there, "
                if (vf == "ni") then
                    pt = pt .. "não "
                    en = en .. "don’t "
                end
                en = en .. "'''"
            end


            pt = pt .. "'''" .. ww .. "'''"
            en = en .. g .. "'''"
            if (tr == true) then
                pt = pt .. " " .. obj
                en = en .. " " .. objtr
            end




        else
            if (not(vf=="pss" or vf=="ips" or vf=="fts" or vf=="i")) then
                pt = pt .. capitalise(get_pron("pt", p, n, "s", imp))
                en = en .. capitalise(get_pron("en", p, n, "s", imp))
            elseif (vf == "i") then
                pt = pt .. "Aquilo foi feito para "
                en = en .. "That was done for "
                pt = pt .. get_pron("pt", p, n, "s", imp)
                en = en .. get_pron("en", p, n, "o", imp)
            else
                if (vf == "pss") then
                    pt = pt .. "É importante que "
                    en = en .. "It’s important that "
                elseif (vf == "ips") then
                    pt = pt .. "Seria bom se "
                    en = en .. "It would be nice if "
                else
                    pt = pt .. "Estará terminado quando "
                    en = en .. "It will be over when "
                end
                pt = pt .. get_pron("pt", p, n, "s", imp)
                en = en .. get_pron("en", p, n, "s", imp)
            end


            if (vf == "ipi") then
                pt = pt .. " frequentemente"
                en = en .. " often '''"
            elseif (vf == "pli") then
                en = en .. " '''had "
            elseif (vf == "fti") then
                en = en .. " '''will "
            elseif (vf == "c") then
                en = en .. " '''would "
            elseif (vf == "i") then
                en = en .. " '''to "
            else
                en = en .. " '''"
            end

            pt = pt .. " '''" .. w .. "'''"
            en = en .. g .. "'''"

            if (tr == true) then
                pt = pt .. " " .. obj
                en = en .. " " .. objtr
            end

            if (vf == "pti") then
                pt = pt .. " uma vez"
                en = en .. " once"
            end
        end


        if (p == "2" or (vf == "ni" or vf == "ai") and p == "3") then
            if (n == "s") then
                pt = pt .. " sozinho"
                en = en .. " by yourself"
            else
                pt = pt .. " sozinhos"
                en = en .. " by yourselves"
            end
        end

        if (vf == "c") then
            pt = pt .. ", se fosse possível"
            en = en .. ", if it were possible"
        end


    end


    return "\n#: ''" .. pt .. ".''\n#:: " .. en .. "."
end

-- Returns a pronoun. Parameters:
--- l: language (“pt” or “en”)
--- p: person (“1”, “2” or “3”)
--- n: number (“s” or “p”)
--- d: declension (“s” for subjective, “o” for objective.) Only relevant for
---    English, Portuguese always returns subjective.
--- imp: whether the verb is impersonal
-- Does not return “você” or “vocês,” these must be added manually.
function get_pron(l, p, n, d, imp)
    if (l == "en") then
        if (p == "1") then
            if (n == "s") then
                if (d == "s") then return "I"
                else return "me" end
            else
                if (d == "s") then return "we"
                else return "us" end
            end
        elseif (p == "2") then return "you"
        else
            if (n == "s") then

                if (imp == true) then return "it"
                else
                    if (d == "s") then return "he"
                    else return "him" end
                end
            else
                if (d == "s") then return "they"
                else return "them" end
            end
        end
    else
        if (p == "1") then
            if (n == "s") then return "eu"
            else return "nós" end
        elseif (p == "2") then
            if (n == "s") then return "tu"
            else return "vós" end
        else
            if (imp == true) then return "ele" end
            if (n == "s") then return "ele"
            else return "eles" end
        end
    end
end

-- Returns the parameter string with its first letter uppercase.
function capitalise(s)
    return mw.ustring.upper(mw.ustring.sub(s, 1, 1)) .. mw.ustring.sub(s, 2);
end

return export
