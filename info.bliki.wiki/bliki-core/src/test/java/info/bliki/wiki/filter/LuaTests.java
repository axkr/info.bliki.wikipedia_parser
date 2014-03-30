package info.bliki.wiki.filter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import junit.framework.TestCase;

public class LuaTests extends TestCase {
	final static String MODULE_HTMLBUILDER = "-- Experimental module for building complex HTML (e.g. infoboxes, navboxes) using a fluent interface\n" +
			"\n" +
			"local HtmlBuilder = {}\n" +
			"\n" +
			"local metatable = {}\n" +
			"\n" +
			"metatable.__index = function(t, key)\n" +
			"    local ret = rawget(t, key)\n" +
			"    if ret then\n" +
			"        return ret\n" +
			"    end\n" +
			"    \n" +
			"    ret = metatable[key]\n" +
			"    if type(ret) == 'function' then\n" +
			"        return function(...) \n" +
			"            return ret(t, ...) \n" +
			"        end \n" +
			"    else\n" +
			"        return ret\n" +
			"    end\n" +
			"end\n" +
			"\n" +
			"metatable.__tostring = function(t)\n" +
			"    local ret = {}\n" +
			"    t._build(ret)\n" +
			"    return table.concat(ret, '')\n" +
			"end\n" +
			"\n" +
			"metatable._build = function(t, ret)\n" +
			"    if t.tagName then \n" +
			"        table.insert(ret, '<' .. t.tagName)\n" +
			"        for i, attr in ipairs(t.attributes) do\n" +
			"            table.insert(ret, ' ' .. attr.name .. '=\"' .. attr.val .. '\"') \n" +
			"        end\n" +
			"        if #t.styles > 0 then\n" +
			"            table.insert(ret, ' style=\"')\n" +
			"            for i, prop in ipairs(t.styles) do\n" +
			"                if type(prop) == 'string' then -- added with cssText()\n" +
			"                    table.insert(ret, prop .. ';')\n" +
			"                else -- added with css()\n" +
			"                    table.insert(ret, prop.name .. ':' .. prop.val .. ';')\n" +
			"                end\n" +
			"            end\n" +
			"            table.insert(ret, '\"')\n" +
			"        end\n" +
			"        if t.selfClosing then\n" +
			"            table.insert(ret, ' /')\n" +
			"        end\n" +
			"        table.insert(ret, '>') \n" +
			"    end\n" +
			"    for i, node in ipairs(t.nodes) do\n" +
			"        if node then\n" +
			"            if type(node) == 'table' then\n" +
			"                node._build(ret)\n" +
			"            else\n" +
			"                table.insert(ret, tostring(node))\n" +
			"            end\n" +
			"        end\n" +
			"    end\n" +
			"    if t.tagName and not t.unclosed and not t.selfClosing then\n" +
			"        table.insert(ret, '</' .. t.tagName .. '>')\n" +
			"    end\n" +
			"end\n" +
			"   \n" +
			"metatable.node = function(t, builder)\n" +
			"    if builder then\n" +
			"        table.insert(t.nodes, builder)\n" +
			"    end\n" +
			"    return t\n" +
			"end\n" +
			"\n" +
			"metatable.wikitext = function(t, ...) \n" +
			"    local vals = {...}\n" +
			"    for i = 1, #vals do\n" +
			"        if vals[i] then\n" +
			"            table.insert(t.nodes, vals[i])\n" +
			"        end\n" +
			"    end\n" +
			"    return t\n" +
			"end\n" +
			"\n" +
			"metatable.newline = function(t)\n" +
			"    table.insert(t.nodes, '\\n')\n" +
			"    return t\n" +
			"end\n" +
			"\n" +
			"metatable.tag = function(t, tagName, args)\n" +
			"    args = args or {}\n" +
			"    args.parent = t\n" +
			"    local builder = HtmlBuilder.create(tagName, args)\n" +
			"    table.insert(t.nodes, builder)\n" +
			"    return builder\n" +
			"end\n" +
			"\n" +
			"function getAttr(t, name)\n" +
			"    for i, attr in ipairs(t.attributes) do\n" +
			"        if attr.name == name then\n" +
			"            return attr\n" +
			"        end\n" +
			"    end\n" +
			"end\n" +
			"\n" +
			"metatable.attr = function(t, name, val)\n" +
			"    -- if caller sets the style attribute explicitly, then replace all styles previously added with css() and cssText()\n" +
			"    if name == 'style' then\n" +
			"        t.styles = {val}\n" +
			"        return t\n" +
			"    end\n" +
			"    \n" +
			"    local attr = getAttr(t, name)\n" +
			"    if attr then\n" +
			"        attr.val = val\n" +
			"    else\n" +
			"        table.insert(t.attributes, {name = name, val = val})\n" +
			"    end\n" +
			"    \n" +
			"    return t\n" +
			"end\n" +
			"\n" +
			"metatable.addClass = function(t, class)\n" +
			"    if class then\n" +
			"        local attr = getAttr(t, 'class')\n" +
			"        if attr then\n" +
			"            attr.val = attr.val .. ' ' .. class\n" +
			"        else\n" +
			"            t.attr('class', class)\n" +
			"        end\n" +
			"    end\n" +
			"\n" +
			"    return t\n" +
			"end\n" +
			"\n" +
			"metatable.css = function(t, name, val)\n" +
			"    if type(val) == 'string' or type(val) == 'number' then\n" +
			"        for i, prop in ipairs(t.styles) do\n" +
			"            if prop.name == name then\n" +
			"                prop.val = val\n" +
			"                return t\n" +
			"            end\n" +
			"        end\n" +
			"        \n" +
			"        table.insert(t.styles, {name = name, val = val})\n" +
			"    end\n" +
			"    \n" +
			"    return t\n" +
			"end\n" +
			"\n" +
			"metatable.cssText = function(t, css)\n" +
			"    if css then\n" +
			"        table.insert(t.styles, css)\n" +
			"    end\n" +
			"    return t\n" +
			"end\n" +
			"\n" +
			"metatable.done = function(t)\n" +
			"    return t.parent or t\n" +
			"end\n" +
			"\n" +
			"metatable.allDone = function(t)\n" +
			"    while t.parent do\n" +
			"        t = t.parent\n" +
			"    end\n" +
			"    return t\n" +
			"end\n" +
			"\n" +
			"function HtmlBuilder.create(tagName, args)\n" +
			"    args = args or {}\n" +
			"    local builder = {}\n" +
			"    setmetatable(builder, metatable)\n" +
			"    builder.nodes = {}\n" +
			"    builder.attributes = {}\n" +
			"    builder.styles = {}\n" +
			"    builder.tagName = tagName\n" +
			"    builder.parent = args.parent\n" +
			"    builder.unclosed = args.unclosed or false\n" +
			"    builder.selfClosing = args.selfClosing or false\n" +
			"    return builder\n" +
			"end\n" +
			"\n" +
			"return HtmlBuilder\n" +
			"";

	final static String MODULE_INFOBOX = "--\n" +
			"-- This module implements {{Infobox}}\n" +
			"--\n" +
			" \n" +
			"local p = {}\n" +
			" \n" +
			"local HtmlBuilder = require('Module:HtmlBuilder')\n" +
			" \n" +
			"local args = {}\n" +
			"local origArgs\n" +
			"local root\n" +
			" \n" +
			"function union(t1, t2)\n" +
			"    -- Returns the union of the values of two tables, as a sequence.\n" +
			"    local vals = {}\n" +
			"    for k, v in pairs(t1) do\n" +
			"        vals[v] = true\n" +
			"    end\n" +
			"    for k, v in pairs(t2) do\n" +
			"        vals[v] = true\n" +
			"    end\n" +
			"    local ret = {}\n" +
			"    for k, v in pairs(vals) do\n" +
			"        table.insert(ret, k)\n" +
			"    end\n" +
			"    return ret\n" +
			"end\n" +
			"\n" +
			"local function getArgNums(prefix)\n" +
			"    -- Returns a table containing the numbers of the arguments that exist\n" +
			"    -- for the specified prefix. For example, if the prefix was 'data', and\n" +
			"    -- 'data1', 'data2', and 'data5' exist, it would return {1, 2, 5}.\n" +
			"    local nums = {}\n" +
			"    for k, v in pairs(args) do\n" +
			"        local num = tostring(k):match('^' .. prefix .. '([1-9]%d*)$')\n" +
			"        if num then table.insert(nums, tonumber(num)) end\n" +
			"    end\n" +
			"    table.sort(nums)\n" +
			"    return nums\n" +
			"end\n" +
			"\n" +
			"local function addRow(rowArgs)\n" +
			"    -- Adds a row to the infobox, with either a header cell\n" +
			"    -- or a label/data cell combination.\n" +
			"    if rowArgs.header then\n" +
			"        root\n" +
			"            .tag('tr')\n" +
			"                .tag('th')\n" +
			"                    .attr('colspan', 2)\n" +
			"                    .addClass(rowArgs.class)\n" +
			"                    .css('text-align', 'center')\n" +
			"                    .cssText(args.headerstyle)\n" +
			"                    .wikitext(rowArgs.header)\n" +
			"    elseif rowArgs.data then\n" +
			"        local row = root.tag('tr')\n" +
			"        row.addClass(rowArgs.rowclass)\n" +
			"        if rowArgs.label then\n" +
			"            row\n" +
			"                .tag('th')\n" +
			"                    .attr('scope', 'row')\n" +
			"                    .css('text-align', 'left')\n" +
			"                    .cssText(args.labelstyle)\n" +
			"                    .wikitext(rowArgs.label)\n" +
			"                    .done()\n" +
			"        end\n" +
			"        \n" +
			"        local dataCell = row.tag('td')\n" +
			"        if not rowArgs.label then \n" +
			"            dataCell\n" +
			"                .attr('colspan', 2)\n" +
			"                .css('text-align', 'center') \n" +
			"        end\n" +
			"        dataCell\n" +
			"            .addClass(rowArgs.class)\n" +
			"            .cssText(rowArgs.datastyle)\n" +
			"            .newline()\n" +
			"            .wikitext(rowArgs.data)\n" +
			"    end\n" +
			"end\n" +
			"\n" +
			"local function renderTitle()\n" +
			"    if not args.title then return end\n" +
			"\n" +
			"    root\n" +
			"        .tag('caption')\n" +
			"            .addClass(args.titleclass)\n" +
			"            .cssText(args.titlestyle)\n" +
			"            .wikitext(args.title)\n" +
			"end\n" +
			"\n" +
			"local function renderAboveRow()\n" +
			"    if not args.above then return end\n" +
			"    \n" +
			"    root\n" +
			"        .tag('tr')\n" +
			"            .tag('th')\n" +
			"                .attr('colspan', 2)\n" +
			"                .addClass(args.aboveclass)\n" +
			"                .css('text-align', 'center')\n" +
			"                .css('font-size', '125%')\n" +
			"                .css('font-weight', 'bold')\n" +
			"                .cssText(args.abovestyle)\n" +
			"                .wikitext(args.above)\n" +
			"end\n" +
			"\n" +
			"local function renderBelowRow()\n" +
			"    if not args.below then return end\n" +
			"    \n" +
			"    root\n" +
			"        .tag('tr')\n" +
			"            .tag('td')\n" +
			"                .attr('colspan', '2')\n" +
			"                .addClass(args.belowclass)\n" +
			"                .css('text-align', 'center')\n" +
			"                .cssText(args.belowstyle)\n" +
			"                .newline()\n" +
			"                .wikitext(args.below)\n" +
			"end\n" +
			"\n" +
			"local function renderSubheaders()\n" +
			"    if args.subheader then\n" +
			"        args.subheader1 = args.subheader\n" +
			"    end\n" +
			"    if args.subheaderrowclass then\n" +
			"        args.subheaderrowclass1 = args.subheaderrowclass\n" +
			"    end\n" +
			"    local subheadernums = getArgNums('subheader')\n" +
			"    for k, num in ipairs(subheadernums) do\n" +
			"        addRow({\n" +
			"            data = args['subheader' .. tostring(num)],\n" +
			"            datastyle = args.subheaderstyle or args['subheaderstyle' .. tostring(num)],\n" +
			"            class = args.subheaderclass,\n" +
			"            rowclass = args['subheaderrowclass' .. tostring(num)]\n" +
			"        })\n" +
			"    end\n" +
			"end\n" +
			"\n" +
			"local function renderImages()\n" +
			"    if args.image then\n" +
			"        args.image1 = args.image\n" +
			"    end\n" +
			"    if args.caption then\n" +
			"        args.caption1 = args.caption\n" +
			"    end\n" +
			"    local imagenums = getArgNums('image')\n" +
			"    for k, num in ipairs(imagenums) do\n" +
			"        local caption = args['caption' .. tostring(num)]\n" +
			"        local data = HtmlBuilder.create().wikitext(args['image' .. tostring(num)])\n" +
			"        if caption then\n" +
			"            data\n" +
			"                .tag('br', {selfClosing = true})\n" +
			"                    .done()\n" +
			"                .tag('div')\n" +
			"                    .cssText(args.captionstyle)\n" +
			"                    .wikitext(caption)\n" +
			"        end\n" +
			"        addRow({\n" +
			"            data = tostring(data),\n" +
			"            datastyle = args.imagestyle,\n" +
			"            class = args.imageclass,\n" +
			"            rowclass = args['imagerowclass' .. tostring(num)]\n" +
			"        })\n" +
			"    end\n" +
			"end\n" +
			"\n" +
			"local function renderRows()\n" +
			"    -- Gets the union of the header and data argument numbers,\n" +
			"    -- and renders them all in order using addRow.\n" +
			"    local rownums = union(getArgNums('header'), getArgNums('data'))\n" +
			"    table.sort(rownums)\n" +
			"    for k, num in ipairs(rownums) do\n" +
			"        addRow({\n" +
			"            header = args['header' .. tostring(num)],\n" +
			"            label = args['label' .. tostring(num)],\n" +
			"            data = args['data' .. tostring(num)],\n" +
			"            datastyle = args.datastyle,\n" +
			"            class = args['class' .. tostring(num)],\n" +
			"            rowclass = args['rowclass' .. tostring(num)]\n" +
			"        })\n" +
			"    end\n" +
			"end\n" +
			"\n" +
			"local function renderNavBar()\n" +
			"    if not args.name then return end\n" +
			"    \n" +
			"    root\n" +
			"        .tag('tr')\n" +
			"            .tag('td')\n" +
			"                .attr('colspan', '2')\n" +
			"                .css('text-align', 'right')\n" +
			"                .wikitext(mw.getCurrentFrame():expandTemplate({ \n" +
			"                    title = 'navbar', \n" +
			"                    args = { args.name, mini = 1 }\n" +
			"                }))\n" +
			"end\n" +
			"\n" +
			"local function renderItalicTitle()\n" +
			"    local italicTitle = args['italic title'] and mw.ustring.lower(args['italic title'])\n" +
			"    if italicTitle == '' or italicTitle == 'force' or italicTitle == 'yes' then\n" +
			"        root.wikitext(mw.getCurrentFrame():expandTemplate({title = 'italic title'}))\n" +
			"    end\n" +
			"end\n" +
			"\n" +
			"local function renderTrackingCategories()\n" +
			"    if args.decat ~= 'yes' then\n" +
			"        if #(getArgNums('data')) == 0 and mw.title.getCurrentTitle().namespace == 0 then\n" +
			"            root.wikitext('[[Category:Articles which use infobox templates with no data rows]]')\n" +
			"        end\n" +
			"        if args.child == 'yes' and args.title then\n" +
			"            root.wikitext('[[Category:Articles which use embedded infobox templates with the title parameter]]')\n" +
			"        end\n" +
			"    end\n" +
			"end\n" +
			"\n" +
			"local function _infobox()\n" +
			"    -- Specify the overall layout of the infobox, with special settings\n" +
			"    -- if the infobox is used as a 'child' inside another infobox.\n" +
			"    if args.child ~= 'yes' then\n" +
			"        root = HtmlBuilder.create('table')\n" +
			"        \n" +
			"        root\n" +
			"            .addClass('infobox')\n" +
			"            .addClass(args.bodyclass)\n" +
			"            .attr('cellspacing', 3)\n" +
			"            .css('border-spacing', '3px')\n" +
			"            \n" +
			"            if args.subbox == 'yes' then\n" +
			"                root\n" +
			"                    .css('padding', '0')\n" +
			"                    .css('border', 'none')\n" +
			"                    .css('margin', '-3px')\n" +
			"                    .css('width', 'auto')\n" +
			"                    .css('min-width', '100%')\n" +
			"                    .css('font-size', '100%')\n" +
			"                    .css('clear', 'none')\n" +
			"                    .css('float', 'none')\n" +
			"                    .css('background-color', 'transparent')\n" +
			"            else\n" +
			"                root\n" +
			"                    .css('width', '22em')\n" +
			"            end\n" +
			"        root\n" +
			"            .cssText(args.bodystyle)\n" +
			"    \n" +
			"        renderTitle()\n" +
			"        renderAboveRow()\n" +
			"    else\n" +
			"        root = HtmlBuilder.create()\n" +
			"        \n" +
			"        root\n" +
			"            .wikitext(args.title)\n" +
			"    end\n" +
			"\n" +
			"    renderSubheaders()\n" +
			"    renderImages() \n" +
			"    renderRows() \n" +
			"    renderBelowRow()  \n" +
			"    renderNavBar()\n" +
			"    renderItalicTitle()\n" +
			"    renderTrackingCategories()\n" +
			"    \n" +
			"    return tostring(root)\n" +
			"end\n" +
			"\n" +
			"local function preprocessSingleArg(argName)\n" +
			"    -- If the argument exists and isn't blank, add it to the argument table.\n" +
			"    -- Blank arguments are treated as nil to match the behaviour of ParserFunctions.\n" +
			"    if origArgs[argName] and origArgs[argName] ~= '' then\n" +
			"        args[argName] = origArgs[argName]\n" +
			"    end\n" +
			"end\n" +
			"\n" +
			"local function preprocessArgs(prefixTable, step)\n" +
			"    -- Assign the parameters with the given prefixes to the args table, in order, in batches\n" +
			"    -- of the step size specified. This is to prevent references etc. from appearing in the\n" +
			"    -- wrong order. The prefixTable should be an array containing tables, each of which has\n" +
			"    -- two possible fields, a \"prefix\" string and a \"depend\" table. The function always parses\n" +
			"    -- parameters containing the \"prefix\" string, but only parses parameters in the \"depend\"\n" +
			"    -- table if the prefix parameter is present and non-blank.\n" +
			"    if type(prefixTable) ~= 'table' then\n" +
			"        error(\"Non-table value detected for the prefix table\", 2)\n" +
			"    end\n" +
			"    if type(step) ~= 'number' then\n" +
			"        error(\"Invalid step value detected\", 2)\n" +
			"    end\n" +
			"    \n" +
			"    -- Get arguments without a number suffix, and check for bad input.\n" +
			"    for i,v in ipairs(prefixTable) do\n" +
			"        if type(v) ~= 'table' or type(v.prefix) ~= \"string\" or (v.depend and type(v.depend) ~= 'table') then\n" +
			"            error('Invalid input detected to preprocessArgs prefix table', 2)\n" +
			"        end\n" +
			"        preprocessSingleArg(v.prefix)\n" +
			"        -- Only parse the depend parameter if the prefix parameter is present and not blank.\n" +
			"        if args[v.prefix] and v.depend then\n" +
			"            for j, dependValue in ipairs(v.depend) do\n" +
			"                if type(dependValue) ~= 'string' then\n" +
			"                    error('Invalid \"depend\" parameter value detected in preprocessArgs')\n" +
			"                end\n" +
			"                preprocessSingleArg(dependValue)\n" +
			"            end\n" +
			"        end\n" +
			"    end\n" +
			"\n" +
			"    -- Get arguments with number suffixes.\n" +
			"    local a = 1 -- Counter variable.\n" +
			"    local moreArgumentsExist = true\n" +
			"    while moreArgumentsExist == true do\n" +
			"        moreArgumentsExist = false\n" +
			"        for i = a, a + step - 1 do\n" +
			"            for j,v in ipairs(prefixTable) do\n" +
			"                local prefixArgName = v.prefix .. tostring(i)\n" +
			"                if origArgs[prefixArgName] then\n" +
			"                    moreArgumentsExist = true -- Do another loop if any arguments are found, even blank ones.\n" +
			"                    preprocessSingleArg(prefixArgName)\n" +
			"                end\n" +
			"                -- Process the depend table if the prefix argument is present and not blank, or\n" +
			"                -- we are processing \"prefix1\" and \"prefix\" is present and not blank, and\n" +
			"                -- if the depend table is present.\n" +
			"                if v.depend and (args[prefixArgName] or (i == 1 and args[v.prefix])) then\n" +
			"                    for j,dependValue in ipairs(v.depend) do\n" +
			"                        local dependArgName = dependValue .. tostring(i)\n" +
			"                        preprocessSingleArg(dependArgName)\n" +
			"                    end\n" +
			"                end\n" +
			"            end\n" +
			"        end\n" +
			"        a = a + step\n" +
			"    end\n" +
			"end\n" +
			" \n" +
			"function p.infobox(frame)\n" +
			"    -- If called via #invoke, use the args passed into the invoking template.\n" +
			"    -- Otherwise, for testing purposes, assume args are being passed directly in.\n" +
			"    if frame == mw.getCurrentFrame() then\n" +
			"        origArgs = frame:getParent().args\n" +
			"    else\n" +
			"        origArgs = frame\n" +
			"    end\n" +
			"    \n" +
			"    -- Parse the data parameters in the same order that the old {{infobox}} did, so that\n" +
			"    -- references etc. will display in the expected places. Parameters that depend on\n" +
			"    -- another parameter are only processed if that parameter is present, to avoid\n" +
			"    -- phantom references appearing in article reference lists.\n" +
			"    preprocessSingleArg('child')\n" +
			"    preprocessSingleArg('bodyclass')\n" +
			"    preprocessSingleArg('subbox')\n" +
			"    preprocessSingleArg('bodystyle')\n" +
			"    preprocessSingleArg('title')\n" +
			"    preprocessSingleArg('titleclass')\n" +
			"    preprocessSingleArg('titlestyle')\n" +
			"    preprocessSingleArg('above')\n" +
			"    preprocessSingleArg('aboveclass')\n" +
			"    preprocessSingleArg('abovestyle')\n" +
			"    preprocessArgs({\n" +
			"        {prefix = 'subheader', depend = {'subheaderstyle', 'subheaderrowclass'}}\n" +
			"    }, 10)\n" +
			"    preprocessSingleArg('subheaderstyle')\n" +
			"    preprocessSingleArg('subheaderclass')\n" +
			"    preprocessArgs({\n" +
			"        {prefix = 'image', depend = {'caption', 'imagerowclass'}}\n" +
			"    }, 10)\n" +
			"    preprocessSingleArg('captionstyle')\n" +
			"    preprocessSingleArg('imagestyle')\n" +
			"    preprocessSingleArg('imageclass')\n" +
			"    preprocessArgs({\n" +
			"        {prefix = 'header'},\n" +
			"        {prefix = 'data', depend = {'label', 'rowclass'}},\n" +
			"        {prefix = 'class'}\n" +
			"    }, 50)\n" +
			"    preprocessSingleArg('headerstyle')\n" +
			"    preprocessSingleArg('labelstyle')\n" +
			"    preprocessSingleArg('datastyle')\n" +
			"    preprocessSingleArg('below')\n" +
			"    preprocessSingleArg('belowclass')\n" +
			"    preprocessSingleArg('belowstyle')\n" +
			"    preprocessSingleArg('name')\n" +
			"    args['italic title'] = origArgs['italic title'] -- different behaviour if blank or absent\n" +
			"    preprocessSingleArg('decat')\n" +
			" \n" +
			"    return _infobox()\n" +
			"end\n" +
			" \n" +
			"return p\n" +
			"";
	public LuaTests(String name) {
		super(name);
	}

	public static void testLua() {
		ScriptEngineManager mgr = new ScriptEngineManager();
		ScriptEngine e = mgr.getEngineByName("luaj");
		e.put("x", 25);
		try {
//			e.eval("y = math.sqrt(x)");
			e.eval(MODULE_HTMLBUILDER);
			e.eval(MODULE_INFOBOX);
			System.out.println( "y="+e.get("y") );
		} catch (ScriptException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


//		String helloWorld = "print( 'hello, world' );";
////		String script = "examples/lua/hello.lua";
//		LuaValue _G = JsePlatform.standardGlobals();
//
//		System.out.println(_G.get("load").call(LuaValue.valueOf(helloWorld)));
	}
}
