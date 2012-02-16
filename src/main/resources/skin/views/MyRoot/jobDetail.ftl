<@extends src="base.ftl">
<@block name="header_scripts">
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

<@block name="header">
<h1>${job.title} N°${job.job.number} (Section ${job.job.section})</h1>
</@block>

<@block name="content">

<div class="jobDetail">
<div>
<h2>Corps</h2>
${job.job.profession}
<h2>Profil</h2>
${job.job.qualification}
<h2>Departement d'enseignement</h2>
${job.job.department}
<h2>Laboratoire de recherche</h2>
${job.job.laboratory}
<h2>Lacalisation</h2>
<span class="like-pre">${job.job.location}</span>
<h2>Contact</h2>
${job.job.contact}
<h2>Site Internet</h2>
<a href="${job.job.website}">${job.job.website}</a>
</div>

<div>
<h2>Profil Enseignement</h2>
<span class="like-pre">${job.job.teaching_profile_fr}</span>
<h2>Teaching profile</h2>
<span class="like-pre">${job.job.teaching_profile_en}</span>
<h2>Contact</h2>
${job.job.teaching_contact}
</div>

<div>
<h2>Profil Recherche</h2>
<span class="like-pre">${job.job.search_profile_fr}</span>
<h2>Search profile</h2>
<span class="like-pre">${job.job.search_profile_en}</span>
<h2>Contact</h2>
${job.job.search_contact}
</div>
</div>

<hr/>

<form method="post" action="." onSubmit="javascript:return checkEmail()">
<fieldset>
<legend>Formulaire de candidature</legend>
<div class="textField">
    <label for="firstname">Prénom: </label>
    <input type="text" name="firstname" />
</div>
<div class="textField">
    <label for="lastname">Nom: </label>
    <input type="text" name="lastname" />
</div>
<div class="textField">
    <label for="email">Email: </label>
    <input type="text" id="email" name="email" />
</div>
<div>
    <input type="submit" value="Candidater" />
</div>
</fieldset>
</form>

</@block>
</@extends>
