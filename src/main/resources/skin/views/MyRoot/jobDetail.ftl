<@extends src="base.ftl">
<@block name="">
<script language="javascript">
function checkEmail() {
var email = document.getElementById('email');
var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
if (!filter.test(email.value)) {
alert('Please provide a valid email address');
email.focus;
return false;
}
}
</script>
</@block>

<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">

<h1>Job detail: ${job.title} - ${job.job.number}</h1>

<form method="post" action=".">
<div>
    <label for="firstname">Firstname: </label>
    <input type="text" name="firstname" />
</div>
<div>
    <label for="lastname">Lastname: </label>
    <input type="text" name="lastname" />
</div>
<div>
    <label for="email">Email: </label>
    <input type="text" id="email" name="email" />
</div>
<div>
    <input type="submit" value="Apply" />
</div>
</form>

</@block>
</@extends>
