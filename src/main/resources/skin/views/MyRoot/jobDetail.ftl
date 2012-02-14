<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">

<h1>Job detail: ${job.title} - ${job.job_container.number}</h1>

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
    <input type="text" name="email" />
</div>
<div>
    <input type="submit" value="Apply" />
</div>
</form>

</@block>
</@extends>
