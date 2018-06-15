BEGIN {
  parse_next = 0
  internal = 0
  external_out = 0
  external_in = 0
  outb = 0
  inb = 0
  other_ip = ""
}
parse_next {
#  print $0
  inb = $3
  other_ip = $1

  multiplier = 1
  if (inb ~ "KB") {
    multiplier = 1024
  }
  if (inb ~ "MB") {
    multiplier = 1024 * 1024
  }
  sub(/KB/, "", inb)
  sub(/MB/, "", inb)
  sub(/B/, "", inb)
  inb = multiplier * inb

  if (other_ip ~ "^172.") {
    internal += inb
    internal += outb
  } else {
    external_out += outb
    external_in += inb
  }
#  printf "%s %.0f %.0f\n", other_ip, inb, outb

  parse_next = 0
}
$0 ~ " "ip" " {
#  print $0
  outb = $4

  multiplier = 1
  if (outb ~ "KB") {
    multiplier = 1024
  }
  if (outb ~ "MB") {
    multiplier = 1024 * 1024
  }
  sub(/KB/, "", outb)
  sub(/MB/, "", outb)
  sub(/B/, "", outb)
  outb = multiplier * outb

  parse_next = 1
}

/^Peak rate/ {
  printf "%s %.0f %.0f %.0f\n", ip, internal, external_out, external_in
  internal = 0
  external_out = 0
  external_in = 0
}
