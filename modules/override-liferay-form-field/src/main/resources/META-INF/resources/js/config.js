// config.js
;(function () {
    var base = MODULE_PATH + '/js/';
    AUI()
        .applyConfig({
            groups: {
                OverrideDDMWeb: {
                    base: base,
                    combine: Liferay.AUI.getCombine(),
                    filter: Liferay.AUI.getFilterConfig(),
                    modules: {
                        'liferay-portlet-dynamic-data-mapping-override': { // Override module name
                            path: 'custom-ddm.js', // Override your custom javascript file name
                            condition: {
                                name: 'liferay-portlet-dynamic-data-mapping-override',
                                trigger: 'liferay-portlet-dynamic-data-mapping', // Give original module name
                                when: 'instead'
                            }
                        },
                        root: base
                    }
                }
            }
        })();
})
