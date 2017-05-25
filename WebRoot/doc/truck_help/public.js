    //点击列表显示说明文字       
   $(function() {
        $('.one').click(function(){
            if ($('.list_hide').hasClass('hide')) {
                    $('.list_hide').removeClass('hide');
                } else{
                    $('.list_hide').addClass('hide');
                }
        })
        $('.two').click(function(){
            if ($('.list_hidee').hasClass('hidee')) {
                    $('.list_hidee').removeClass('hidee');
                } else{
                    $('.list_hidee').addClass('hidee');
                }
        })
        $('.there').click(function(){
            if ($('.list_there').hasClass('there3')) {
                    $('.list_there').removeClass('there3');
                } else{
                    $('.list_there').addClass('there3');
                }
        })
    });     

   //点击li背景色样式变换
   /*$(function() {
        $('.list_two li').click(function(){
             $(this).css('background', '#e5e5e5')
        })
    }); */  


    //右键头向下旋转
    $(function(){
        $('.one').click(function(){
            if ($('#target').hasClass('b')) {
                $('#target').removeClass('b');
            } else{
                
                $('#target').addClass('b');
            }
        })
        $('.two').click(function(){
            if ($('#target2').hasClass('b')) {
                $('#target2').removeClass('b');
            } else{                        
                $('#target2').addClass('b');
            }
        })
    })

    //一键回到顶部
    $(function(){
        var $body = $(document.body);
        var $bottomTools = $('.bottom_tools');
        var qrImg = $('.qr_img');
            $(window).scroll(function () {
                var scrollHeight = $(document).height();
                var scrollTop = $(window).scrollTop();
                var $windowHeight = $(window).innerHeight();
                scrollTop > 50 ? $("#scrollUp").fadeIn(200).css("display","block") : $("#scrollUp").fadeOut(200);
            });
            $('#scrollUp').click(function (e) {
                e.preventDefault();
                $('html,body').animate({ scrollTop:0});
            });                
    });