<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">

<#if error??>
  <h1>Votre candidature n'a pas pu etre enregistr&eacute; pour la raison suivante:</h1>
  <p>${error}</p>
  <p>Veuillez r&eacute;essayer, ou contacter l'administrateur technique.</p>
<#else>
  <h1>Votre candidature a correctement &eacute;tait prise en compte</h1>
  <p>Vos identifiants de connexion vous ont &eacute;t&eacute; envoy&eacute;s par mail a l'adresse: ${application.application.email}</p>
  <p>Pour vous connecter et compl&eacute;ter votre dossier de candidature, <a href="${applyUrl}">cliquez ici</a></p>
   
</#if>


</@block>
</@extends>
