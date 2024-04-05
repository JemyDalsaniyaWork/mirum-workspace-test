<html>
<style type="text/css">
    body {
        margin: 0;
        overflow: visible;
        padding: 0;
    }

    #container {
        font-family: helvetica, 'open sans', arial;
        margin: 0 auto 40px;
        width: 660px;
    }

    .email-notification-body-text {
        line-height: 30px;
        margin-bottom: 24px;
        margin-top: 0;
    }

    .field-label {
        color: #535a5e;
        font-size: 18px;
        margin: 32px 0 16px;
    }

    .field-value {
        font-size: 16px;
        margin: 0;
    }

    h1,
    h2 {
        margin-bottom: 24px;
        margin-top: 0;
    }

    h3 {
        color: #474d51;
        font-weight: 300;
        margin: 8px 0;
        text-align: center;
    }

    h4 {
        color: #9aa2a6;
        font-size: 21px;
        font-weight: 500;
        margin: 0;
    }

    .introduction {
        background-color: #fff;
        border-radius: 4px;
        margin: 0 auto 24px;
        padding: 40px;
    }

    table {
        background-color: #e4e9ec;
        padding: 40px;
    }

    .view-form-entries-url {
        color: #0c5d92;
        text-decoration: none;
    }

    .view-form-url {
        background: #0d5b97;
        border-radius: 4px;
        color: #fff;
        display: block;
        padding: 18px;
        text-align: center;
        text-decoration: none;
    }
</style>

<head>
    <title>${formName}</title>
    <meta charset="UTF-8" />
</head>

<body>
<div id="container">
    <table>
        <tr>
            <td>
                <div class="introduction" id="introduction">
                    <h2 align="center">Hi,</h2>

                    <p class="email-notification-body-text">
                        <b>${userName}</b> submitted an entry for the ${formName} form in the ${siteName} site.
                    </p>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <h3>Here's what <b>${userName}</b> entered into the form:</h3>
                <div class="introduction">

                <p class="field-label">Form Instance Record Id:</p>
                <p class="field-value"><pre>${formInstanceRecordId}</pre></p>

                <#foreach page in pages>
                            <h4>${page.title}</h4>

                        <#foreach field in page.fields>


                            <#if field??>
                                    <p class="field-label">${field.label}</p>
                                <p class="field-value"><pre>${field.value}</pre></p>
                            </#if>
                        </#foreach>
                </#foreach>
                </div>
            </td>
        </tr>
    </table>
</div>
</body>
</html>

