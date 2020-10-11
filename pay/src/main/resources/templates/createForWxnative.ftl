<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <title>支付系统</title>
    <script src="https://cdn.bootcss.com/jquery/1.5.1/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/jquery.qrcode/1.0/jquery.qrcode.min.js"></script>

</head>

<body>
<div id="myqrcode"></div>
<div id="orderId" hidden>${orderId}</div>
<div id="returnUrl" hidden>${returnUrl}</div>

<script>

    jQuery('#myqrcode').qrcode({
        text	: "${codeUrl}"
    });

    $(function(){
        //设置定时器
        setInterval(function () {
            console.log("开始查询订单状态")
            $.ajax({
                url:"/pay/queryByOrdrtId",
                data:{
                    orderId:$('#orderId').text()//拿到orderId内容
                },
                success:function (result) {
                    console.log(result)
                    if(result.platformStatus =='SUCCESS'&&
                        result.platformStatus!=null){
                        location.href=$('#returnUrl').text();
                    }
                },
                error:function (result) {
                    console.log(result)
                }
            })
        },2000)

    });
</script>



</body>
</html>
