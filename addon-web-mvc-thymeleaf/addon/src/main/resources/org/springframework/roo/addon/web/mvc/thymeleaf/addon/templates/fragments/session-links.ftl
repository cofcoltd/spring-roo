<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Session info</title>
  </head>
<#if userManagedComponents?has_content && userManagedComponents["body"]??>
  ${userManagedComponents["body"]}
<#else>
  <body id="body">
    <!--
    Only the internal content of the following div is included within
    the template, in session fragment
    -->
    <ul data-layout-fragment="session">
      <!-- User menu -->
        <li class="dropdown">
          <a class="dropdown-toggle" data-toggle="dropdown" role="button"
            aria-haspopup="true" aria-expanded="false">
            <span class="glyphicon glyphicon-user" aria-hidden="true"></span>&nbsp;
            <span class="hidden-sm" data-sec-authentication="principal.username">User</span>
            <span class="caret"></span>
          </a>
          <ul class="dropdown-menu">
           <li><a href="#"><span class="glyphicon glyphicon-wrench" aria-hidden="true"></span>
           &nbsp;<span data-th-text="${r"#{"}label_profile${r"}"}">Admin Profile</span></a></li>
           <li><a href="#"><span class="glyphicon glyphicon-lock" aria-hidden="true"></span>
           &nbsp;<span data-th-text="${r"#{"}label_change_password${r"}"}">Change password</span></a></li>
           <li>
             <form data-th-action="@{/logout}" action="/logout" method="post">
               <button type="submit" class="btn btn-link">
                <span class="glyphicon glyphicon-log-out" aria-hidden="true"></span>
                <span data-th-text="${r"#{"}label_logout ${r"}"}">Log out</span>
               </button>
             </form>
           </li>
         </ul>
        </li>
    </ul>

    <ul data-layout-fragment="links">
      <!-- User menu links -->
      <li><a href="#"><span class="glyphicon glyphicon-envelope" aria-hidden="true"></span>
      &nbsp;<span class="hidden-sm" data-th-text="${r"#{"}label_contact${r"}"}">Contact</span></a></li>
      <li><a href="#"><span class="glyphicon glyphicon-question-sign" aria-hidden="true"></span>
      &nbsp;<span class="hidden-sm" data-th-text="${r"#{"}label_help${r"}"}">Help</span></a></li>
   </ul>

  </body>
  </#if>
</html>