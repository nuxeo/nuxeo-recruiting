<@extends src="base.ftl">
<@block name="header">You signed in as ${Context.principal}</@block>

<@block name="content">

<#if success>
<h2>YATTA</h2>
</#if>

<h1>Job listing</h1>
<ol>
<#list jobs as job>
  <li>
    <div>
        <h2>${job.title} - ${job.job_container.number}</h2>
        ${job.job_container.search_profile} -- ${job.job_container.search_contact}
        Url: <a href="./job/${job.id}/">${job.id}</a>
    </div>
  </li>
</#list>
</ol>

</@block>
</@extends>
