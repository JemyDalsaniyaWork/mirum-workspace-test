[#function getArticleDocument groupId articleId]

   [#assign srv = serviceLocator.findService("com.liferay.journal.service.JournalArticleLocalService")]

   [#assign article = srv.getArticle(groupId,articleId)]

   [#assign document = saxReaderUtil.read(article.getContent())]

   [#return document.getRootElement()]

[/#function]

[#function getArticleDocumentExpress documentContent]

   [#assign document = saxReaderUtil.read(documentContent)]

   [#return document.getRootElement()]

[/#function]

[#function getArticleValue rootElement name type]

   [#attempt]

       [#list rootElement.elements() as dynamicElement ]

           [#if type == "image"]

               [#if dynamicElement.attributeValue("name") == name]

                   [#assign image = dynamicElement.element("dynamic-content").getStringValue()?replace("\\/","/")]

                   [#assign imageObj = image?eval]

                   [#return imageObj.url]

               [/#if]

           [/#if]

           [#if type == "text"]

           [#if dynamicElement.attributeValue("name") == name]

               [#assign text = dynamicElement.element("dynamic-content").getText()]

               [#return text]

           [/#if]

       [/#if]

       [/#list]

       [#recover]

   [/#attempt]

[/#function]

<ul class="jcc-list-container">
    [#if collectionObjectList?? ]
        [#list collectionObjectList as item ]
        [#assign rootElement = getArticleDocumentExpress(item.getContent())]
        <li>
    <!--<pre>${item.getContent()}</pre>-->
    <div>
        ${configuration.customFieldName}
    </div>
    <div>${getArticleValue(rootElement, "${configuration.customFieldName}", "text")!""}</div>
    </li>
        [/#list]
    [/#if]
</ul>