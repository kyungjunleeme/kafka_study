#!/bin/bash

echo "=== Kernel Parameters for Kafka Performance ==="
echo "Timestamp: $(date '+%Y-%m-%d %H:%M:%S')"
echo ""

echo "=== Network Parameters ==="
echo "net.core.wmem_default : $(sysctl -n net.core.wmem_default)"
echo "net.core.wmem_max : $(sysctl -n net.core.wmem_max)"
echo "net.core.rmem_default : $(sysctl -n net.core.rmem_default)"
echo "net.core.rmem_max : $(sysctl -n net.core.rmem_max)"
echo "net.ipv4.tcp_wmem : $(sysctl -n net.ipv4.tcp_wmem)"
echo "net.ipv4.tcp_rmem : $(sysctl -n net.ipv4.tcp_rmem)"
echo "net.ipv4.tcp_max_syn_backlog : $(sysctl -n net.ipv4.tcp_max_syn_backlog)"

echo ""
echo "=== VM Parameters ==="
echo "vm.swappiness : $(sysctl -n vm.swappiness)"
echo "vm.dirty_ratio : $(sysctl -n vm.dirty_ratio)"
echo "vm.dirty_background_ratio : $(sysctl -n vm.dirty_background_ratio)"

echo ""
echo "=== Filesystem Parameters ==="
echo "fs.file-max : $(sysctl -n fs.file-max)"

echo ""
echo "=== Dirty Pages Information ==="
echo "$(grep -E "nr_dirty|nr_writeback|nr_writeback_temp" /proc/vmstat)"
