-- This file is simplification of enwiktionary's "Module:TemplateStyles".
return function (fn_name, fn_args)
    return mw.getCurrentFrame():extensionTag{
        name = fn_name, args = fn_args
    }
end
