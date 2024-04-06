Implementation of Spring Security is done in following classes:
1) UserServiceImpl to save password of user in encrypted form
2) In controller to get the current logged in user through Principal
3) In every class of config package

Important things:
1) Register page ke input fields ko same name do jo entity ki feilds hai means email ko email hi likho register.html mai. Also id dene se khuch nhi hota name dena pdta hai input fields ka.
2) login page ke form mai action vahi daalo jo loginProcessingUrl mai daala hai config mai, and tumhe handle nhi krna voh url springboot handle krega.
3) login page ke email input field mai name email nhi username likhna