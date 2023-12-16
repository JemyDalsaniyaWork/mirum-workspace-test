<html>
<head>

    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="generator" content="">
    <title>Pdf</title>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;500;600;700&display=swap');
        /*@import url('https://fonts.googleapis.com/css2?family=Noto+Naskh+Arabic:wght@400;500;600;700&display=swap');*/

        @page {
            size: a4;
        }

        body {
            margin: 0px;
            padding: 0px;
            font-family: 'Open Sans', sans-serif;
            /*font-family: 'Noto Naskh Arabic', serif;*/

        }

        table {
            width: 100%;
        }

        .invoice-table-main {
            max-width: 1140px;
            margin: 0 auto;
            padding: 30px 0px;
        }

        * {
            margin: 0px;
            padding: 0px;
            box-sizing: border-box;
        }

        .logo-invoice-table {
            width: 100%;
            padding: 0px 120px;
        }

        .logo-invoice-table th {
            border-bottom: 2px solid #EBEDF1;
        }

        .logo-invoice-table th:first-child {
            text-align: left;

        }

        .logo-invoice-table th:last-child {
            text-align: right;
            color: #17658B;
            font-weight: 600;
        }

        .logo-invoice-table td {
            padding: 15px 10px;
        }

        .logo-invoice-table td h2 {
            color: #09172D;
            font-size: 14px;
            font-weight: 600;
            line-height: 25px;
        }

        .logo-invoice-table td p {
            color: #17658B;
            font-size: 16px;
            font-weight: 400;
            line-height: 24px;
        }

        .logo-invoice-table td:first-child {
            text-align: right;

        }

        .logo-invoice-table td:last-child {
            text-align: right;
        }

        .logo-total-invoice-table {
            width: 100%;

        }

        .logo-total-invoice-table th {
            text-align: left;
            color: #287D3C;
            font-weight: 600;
            font-size: 12px;
            line-height: 18px;
            border-bottom: 1px solid #EBEDF1;
        }

        .logo-total-invoice-table tr td {
            background-color: #EBEDF1;
            color: #304F78;
            font-weight: 400;
            font-size: 14px;
            line-height: 25px;

        }

        .logo-total-invoice-table tr:first-child td:first-child {
            color: #37BAC6;
            font-weight: 600;
            font-size: 14px;
            line-height: 25px;
        }

        .logo-total-invoice-table tr:first-child td {
            font-weight: 600;
            background-color: #fff;
        }

        .logo-total-invoice-table th,
        .logo-total-invoice-table td {
            padding: 15px 10px;
        }

        .total-invoice-div {
            background-color: #fff;
            padding: 50px 80px;
            box-shadow: 0 3px 10px rgb(0 0 0 / 0.2);
            max-width: 100%;
            margin-top: 30px;
        }

      /*  @media only print{
            !*@import url('https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;500;600;700&display=swap');*!
            !*@import url('https://fonts.googleapis.com/css2?family=Noto+Naskh+Arabic:wght@400;500;600;700&display=swap');*!
            body {
                font-family: 'Noto Naskh Arabic', serif;
            }
        }*/

    </style>
</head>

<body>
<div class="invoice-table-main">
    <div class="logo-invoice-table">
        <table border="0" cellspacing="0" cellpadding="0">
            <thead>
            <tr>
                <th>
                    <img src="https://liferay.upturn.mirummea.com/documents/d/jcc/jcc-logo">
                </th>
                <th>ﻃﻠﺒﻲ</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>
                    <h2>
                        القيمة الاجمالية
                    </h2>
                    <p>{{orderTotal}} SAR</p>
                </td>
                <td>
                    <h2>
                        ﺮﻘﻣ ﻼﺘﻋﺮﻴﻓ ﻼﺧﺎﺻ بﻼﻄﻠﺑ
                    </h2>
                    <p>{{orderId}}</p>
                </td>
            </tr>
            <tr>
                <td>
                    <h2>
                        إسم الطلب
                    </h2>
                    <p>{{orderName}}</p>
                </td>
                <td>
                    <h2>
                        ﺗﺎﺮﻴﺧ ﻼﻄﻠﺑ
                    </h2>
                    <p>{{orderDate}}</p>
                </td>
            </tr>
            <tr>
                <td>
                    <h2>رقم المعاملة </h2>
                    <p>{{transactionId}}</p>
                </td>
                <td>
                    <h2>
                        ﺮﻘﻣ ﻼﻤﻋﺎﻤﻟﺔ
                    </h2>
                    <p>{{orderStatus}}</p>
                </td>
            </tr>
            </tbody>
        </table>
    </div>


    <div class="total-invoice-div">
        <div class="logo-total-invoice-table">
            <table border="0" cellspacing="0" cellpadding="0" class="logo-total-invoice-table">
                <thead>
                <tr>
                    <th colspan="6">
                        ﻻﺪﻔﻋ ﻼﻧﺎﺠﺣ
                    </th>
                </tr>
                </thead>

                <tbody>
                <tr>
                    <td>
                        القيمة الاجمالية
                    </td>
                    <td>
                        ضريبة القيمة المضافة
                    </td>
                    <td>
                        العدد
                    </td>
                    <td>
                        قيمة الخدمة
                    </td>
                    <!-- <td>
                        SKU
                    </td> -->
                    <td>
                        الإسم
                    </td>
                </tr>
                <tr>
                    <td>
                        {{productTotal}} SAR
                    </td>
                    <td>
                        {{productVAT}} SAR
                    </td>
                    <td>
                        {{productQuantity}}
                    </td>
                    <td>
                        {{productPrice}} SAR
                    </td>
                    <!-- <td>
                        {{productSku}}
                    </td> -->
                    <td>
                        {{productName}}
                    </td>

                </tr>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
