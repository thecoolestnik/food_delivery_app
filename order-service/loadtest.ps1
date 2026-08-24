$orderTemplate = Get-Content order.json -Raw
$totalRequests = 500
$concurrency = 50

$runspacePool = [runspacefactory]::CreateRunspacePool(1, $concurrency)
$runspacePool.Open()

$scriptBlock = {
    param($body)
    try {
        Invoke-RestMethod -Uri "http://localhost:8081/api/orders" -Method Post -Body $body -ContentType "application/json" -TimeoutSec 5 | Out-Null
    } catch {}
}

$startTime = Get-Date
$runspaces = @()

1..$totalRequests | ForEach-Object {
    $ps = [powershell]::Create()
    $ps.RunspacePool = $runspacePool
    $ps.AddScript($scriptBlock).AddArgument($orderTemplate) | Out-Null
    $runspaces += [PSCustomObject]@{ Pipe = $ps; Handle = $ps.BeginInvoke() }
}

foreach ($r in $runspaces) {
    $r.Pipe.EndInvoke($r.Handle)
    $r.Pipe.Dispose()
}

$runspacePool.Close()
$runspacePool.Dispose()

$endTime = Get-Date
$duration = ($endTime - $startTime).TotalSeconds
$rate = $totalRequests / $duration

Write-Host "Sent $totalRequests requests in $([math]::Round($duration,2))s"
Write-Host "Throughput: $([math]::Round($rate,2)) events/sec"
