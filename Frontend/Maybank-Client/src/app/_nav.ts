import { INavData } from '@coreui/angular';
import {MyINavData} from './MyINavData';
import {TypeOfPermissionRoles} from './common/app.enum';

export const navItems: MyINavData[] = [
  {
    name: 'Dashboard',
    url: '/dashboard',
    icon: 'icon-speedometer',
    displayOrder: 1,
    role: 'ROLE_SUPERADMIN',
    permissions: [TypeOfPermissionRoles.roleSuperAdmin],
    badge: {
      variant: 'info',
      text: 'NEW'
    }
  },
  {
    name: 'Transaction Manage',
    url: '/companyManage',
    icon: 'cil-house',
    displayOrder: 2,
    role: 'ROLE_ADMIN',
    permissions: [TypeOfPermissionRoles.roleAdmin],
    children: [
      {
        name: 'Transaction Listing',
        url: '/companyManage/transaction-list',
        icon: 'cil-house'
      }
    ]
  },
  // {
  //   name: 'User Manage',
  //   url: '/userManage',
  //   icon: 'icon-people',
  //   displayOrder: 4,
  //   role: 'ROLE_SUPERADMIN',
  //   permissions: [TypeOfPermissionRoles.roleAdmin],
  //   children: [
  //     {
  //       name: 'End User Listing',
  //       url: '/userManage/end-user-list',
  //       icon: 'icon-people'
  //     },
  //     {
  //       name: 'Auth User Listing',
  //       url: '/userManage/auth-user-list',
  //       icon: 'icon-people'
  //     }
  //   ]
  // },
  // {
  //   name: 'Branch Manage',
  //   url: '/branchManage',
  //   icon: 'cil-lan',
  //   displayOrder: 5,
  //   role: 'ROLE_ADMIN',
  //   permissions: [TypeOfPermissionRoles.roleAdmin],
  //   children: [
  //     {
  //       name: 'Branch Listing',
  //       url: '/branchManage/branch-list',
  //       icon: 'cil-lan'
  //     }
  //   ]
  // },
  // {
  //   name: 'Table Manage',
  //   url: '/tableManage',
  //   icon: 'cil-space-bar',
  //   displayOrder: 7,
  //   role: 'ROLE_ADMIN',
  //   permissions: [TypeOfPermissionRoles.roleAdmin],
  //   children: [
  //     {
  //       name: 'Table Listing',
  //       url: '/tableManage/table-list',
  //       icon: 'cil-space-bar'
  //     }
  //   ]
  // },
  // {
  //   name: 'Rank Manage',
  //   url: '/rankManage',
  //   icon: 'cil-list-numbered-rtl',
  //   displayOrder: 9,
  //   role: 'ROLE_ADMIN',
  //   permissions: [TypeOfPermissionRoles.roleAdmin],
  //   children: [
  //     {
  //       name: 'Rank Type Listing',
  //       url: '/rankManage/rank-type-list',
  //       icon: 'cil-list-numbered-rtl'
  //     },
  //     {
  //       name: 'Rank Detail Listing',
  //       url: '/rankManage/rank-detail-list',
  //       icon: 'cil-list-rich'
  //     },
  //   ]
  // },
  // {
  //   title: true,
  //   name: 'Gift & Promotion',
  //   displayOrder: 12,
  //   role: 'ROLE_ADMIN',
  // },
  // {
  //   name: 'Gift',
  //   url: '/special/gift-listing',
  //   icon: 'cil-gift',
  //   displayOrder: 13,
  //   role: 'ROLE_ADMIN',
  // },
  // {
  //   name: 'Promotion',
  //   url: '/special/promotion-listing',
  //   icon: 'cil-bullhorn',
  //   displayOrder: 14,
  //   role: 'ROLE_ADMIN',
  // },
  // {
  //   title: true,
  //   name: 'Term & Condition',
  //   displayOrder: 15,
  //   role: 'ROLE_ADMIN',
  // },
  // {
  //   name: 'Term & Condition',
  //   url: '/term/term-listing',
  //   icon: 'cil-notes',
  //   displayOrder: 16,
  //   role: 'ROLE_ADMIN',
  // },
  // {
  //   title: true,
  //   name: 'Credit',
  //   displayOrder: 17,
  //   role: 'ROLE_ADMIN',
  // },
  // {
  //   name: 'Credit',
  //   url: '/credit/credit-listing',
  //   icon: 'cil-money',
  //   displayOrder: 18,
  //   role: 'ROLE_ADMIN',
  // },
  // {
  //   name: 'Song',
  //   url: '/song/song-listing',
  //   icon: 'icon-film',
  //   displayOrder: 19,
  //   role: 'ROLE_ADMIN',
  // }
  // {
  //   title: true,
  //   name: 'Theme'
  // },
  // {
  //   name: 'Colors',
  //   url: '/theme/colors',
  //   icon: 'icon-drop'
  // },
  // {
  //   name: 'Typography',
  //   url: '/theme/typography',
  //   icon: 'icon-pencil'
  // },
  // {
  //   title: true,
  //   name: 'Components'
  // },
  // {
  //   name: 'Base',
  //   url: '/base',
  //   icon: 'icon-puzzle',
  //   children: [
  //     {
  //       name: 'Cards',
  //       url: '/base/cards',
  //       icon: 'icon-puzzle'
  //     },
  //     {
  //       name: 'Carousels',
  //       url: '/base/carousels',
  //       icon: 'icon-puzzle'
  //     },
  //     {
  //       name: 'Collapses',
  //       url: '/base/collapses',
  //       icon: 'icon-puzzle'
  //     },
  //     {
  //       name: 'Forms',
  //       url: '/base/forms',
  //       icon: 'icon-puzzle'
  //     },
  //     {
  //       name: 'Navbars',
  //       url: '/base/navbars',
  //       icon: 'icon-puzzle'
  //
  //     },
  //     {
  //       name: 'Pagination',
  //       url: '/base/paginations',
  //       icon: 'icon-puzzle'
  //     },
  //     {
  //       name: 'Popovers',
  //       url: '/base/popovers',
  //       icon: 'icon-puzzle'
  //     },
  //     {
  //       name: 'Progress',
  //       url: '/base/progress',
  //       icon: 'icon-puzzle'
  //     },
  //     {
  //       name: 'Switches',
  //       url: '/base/switches',
  //       icon: 'icon-puzzle'
  //     },
  //     {
  //       name: 'Tables',
  //       url: '/base/tables',
  //       icon: 'icon-puzzle'
  //     },
  //     {
  //       name: 'Tabs',
  //       url: '/base/tabs',
  //       icon: 'icon-puzzle'
  //     },
  //     {
  //       name: 'Tooltips',
  //       url: '/base/tooltips',
  //       icon: 'icon-puzzle'
  //     }
  //   ]
  // },
  // {
  //   name: 'Buttons',
  //   url: '/buttons',
  //   icon: 'icon-cursor',
  //   children: [
  //     {
  //       name: 'Buttons',
  //       url: '/buttons/buttons',
  //       icon: 'icon-cursor'
  //     },
  //     {
  //       name: 'Dropdowns',
  //       url: '/buttons/dropdowns',
  //       icon: 'icon-cursor'
  //     },
  //     {
  //       name: 'Brand Buttons',
  //       url: '/buttons/brand-buttons',
  //       icon: 'icon-cursor'
  //     }
  //   ]
  // },
  // {
  //   name: 'Charts',
  //   url: '/charts',
  //   icon: 'icon-pie-chart'
  // },
  // {
  //   name: 'Icons',
  //   url: '/icons',
  //   icon: 'icon-star',
  //   children: [
  //     {
  //       name: 'CoreUI Icons',
  //       url: '/icons/coreui-icons',
  //       icon: 'icon-star',
  //       badge: {
  //         variant: 'success',
  //         text: 'NEW'
  //       }
  //     },
  //     {
  //       name: 'Flags',
  //       url: '/icons/flags',
  //       icon: 'icon-star'
  //     },
  //     {
  //       name: 'Font Awesome',
  //       url: '/icons/font-awesome',
  //       icon: 'icon-star',
  //       badge: {
  //         variant: 'secondary',
  //         text: '4.7'
  //       }
  //     },
  //     {
  //       name: 'Simple Line Icons',
  //       url: '/icons/simple-line-icons',
  //       icon: 'icon-star'
  //     }
  //   ]
  // },
  // {
  //   name: 'Notifications',
  //   url: '/notifications',
  //   icon: 'icon-bell',
  //   children: [
  //     {
  //       name: 'Alerts',
  //       url: '/notifications/alerts',
  //       icon: 'icon-bell'
  //     },
  //     {
  //       name: 'Badges',
  //       url: '/notifications/badges',
  //       icon: 'icon-bell'
  //     },
  //     {
  //       name: 'Modals',
  //       url: '/notifications/modals',
  //       icon: 'icon-bell'
  //     }
  //   ]
  // },
  // {
  //   name: 'Widgets',
  //   url: '/widgets',
  //   icon: 'icon-calculator',
  //   badge: {
  //     variant: 'info',
  //     text: 'NEW'
  //   }
  // }
];
