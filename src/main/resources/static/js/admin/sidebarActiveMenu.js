$(document).ready(function () {
    const path = window.location.pathname;
    console.log("Current path:", path);
    $('.treeview-menu').hide();

    // Reset trạng thái menu
    function resetMenu() {
        $('.sidebar-menu li').removeClass('active open');
        $('.treeview-menu').hide();
    }

    // Kích hoạt menu
    function activateMenu($item) {
        if (!$item || !$item.length) return false;

        $item.addClass('active');

        // Nếu là submenu, mở menu cha
        if ($item.parent().hasClass('treeview-menu')) {
            const $tree = $item.closest('.treeview');
            $tree.addClass('active open');
            $tree.find('> .treeview-menu').show();
        }

        return true;
    }

    // Highlight theo URL
    function highlightActiveMenu() {
        const currentUrl = window.location.href;
        console.log("Highlighting for URL:", currentUrl);

        // Đánh dấu menu đang hoạt động dựa trên URL
        if (currentUrl.includes('/Admin/order')) {
            $('a[href="/Admin/order"]').parent().addClass('active');
        }
        else if (currentUrl.includes('/Admin/customers')) {
            $('a[href="/Admin/customers"]').parent().addClass('active');
        }
        else if (currentUrl.includes('/Admin/suppliers-manager')) {
            $('#supplier-menu').addClass('active');
        }
        else if (currentUrl.includes('/Admin/ware-houses')) {
            $('#warehouse-menu').addClass('active');
        }
        else if (currentUrl.includes('/Admin/employee-manager')) {
            $('#employee-menu').addClass('active');
        }
        else if (currentUrl.includes('/Admin/category-manager')) {
            $('#category-menu').addClass('active');
        }
        else if (currentUrl.includes('/Admin/brand-manager')) {
            $('#brand-menu').addClass('active');
        }
        else if (currentUrl.includes('/Admin/product-manager')) {
            $('#product-menu').addClass('active');
        }
        else if (currentUrl.includes('/Admin/statistical')) {
            $('#report-menu').addClass('active');
        }
    }
    highlightActiveMenu();

    // Click mở rộng treeview
    $('.treeview > a').on('click', function (e) {
        e.preventDefault();
        const $parent = $(this).parent();
        const $submenu = $(this).next('.treeview-menu');
        const isOpen = $submenu.is(':visible');

        if ($parent.hasClass('active')) {
            if (isOpen) {
                $submenu.slideUp(200);
                $parent.removeClass('open');
            }
        } else {
            $('.treeview-menu').not($submenu).slideUp(200);
            $('.treeview').not($parent).removeClass('open');

            $submenu.slideDown(200);
            $parent.addClass('open');
        }
    });

    // Click menu thường
    $('.sidebar-menu > li:not(.treeview) > a').on('click', function () {
        resetMenu();
        $(this).parent().addClass('active');
    });

    // Click submenu
    $('.treeview-menu > li > a').on('click', function () {
        const $item = $(this).parent();
        const $tree = $item.closest('.treeview');

        $item.siblings().removeClass('active');
        $item.addClass('active');
        $tree.addClass('active open');
        $tree.find('> .treeview-menu').show();
    });

    $('.sidebar-menu li').hover(
        function () {
            if (!$(this).hasClass('active')) {
                $(this).addClass('hover');
                if ($(this).hasClass('treeview')) {
                    $(this).find('> .treeview-menu').stop(true, true).slideDown(200);
                }
            }
        },
        function () {
            $(this).removeClass('hover');
            if ($(this).hasClass('treeview') && !$(this).hasClass('active')) {
                $(this).find('> .treeview-menu').stop(true, true).slideUp(200);
            }
        }
    );
});