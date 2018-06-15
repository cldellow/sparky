ip=$(ip addr show eth0 | awk -F "[ /]" '/inet / {print $6}')
awk -v ip="$ip" '
BEGIN {
  prev_total = 0
  prev_idle = 0
  while (getline < "/proc/stat") {
    close("/proc/stat")
    idle = $5
    total = 0
    for (i=2; i<=NF; i++)
      total += $i
    print systime(), ip":cpu:"(1-(idle-prev_idle)/(total-prev_total))
    prev_idle = idle
    prev_total = total
    system("sleep 1")
  }
}
'
