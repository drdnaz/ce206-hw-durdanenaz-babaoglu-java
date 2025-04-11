import os
import sys
import re
import json

def parse_lcov_data(lcov_data):
    files = {}
    current_file = None
    for line in lcov_data.splitlines():
        line = line.strip()
        if line.startswith("SF:"):
            current_file = line[3:]
            files[current_file] = {'covered': 0, 'total': 0}
        elif line.startswith("DA:"):
            if current_file is None:
                continue
            parts = line[3:].split(',')
            is_covered = int(parts[1]) > 0
            if is_covered:
                files[current_file]['covered'] += 1
            files[current_file]['total'] += 1
    return files

def create_html_report(coverage_data, output_dir):
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    total_lines = sum(data['total'] for data in coverage_data.values())
    covered_lines = sum(data['covered'] for data in coverage_data.values())
    coverage_percent = (covered_lines / total_lines * 100) if total_lines > 0 else 0

    html = '''<!DOCTYPE html>
<html>
<head>
    <title>Doxygen Documentation Coverage Report</title>
    <style>
        body {{ font-family: Arial, sans-serif; margin: 20px; }}
        table {{ border-collapse: collapse; width: 100%; }}
        th, td {{ text-align: left; padding: 8px; border: 1px solid #ddd; }}
        tr:nth-child(even) {{ background-color: #f2f2f2; }}
        th {{ background-color: #4CAF50; color: white; }}
        .low {{ background-color: #ffcccc; }}
        .medium {{ background-color: #ffffcc; }}
        .high {{ background-color: #ccffcc; }}
        h1, h2 {{ color: #333; }}
        .progress-bar {{
            width: 100%;
            background-color: #e0e0e0;
            border-radius: 4px;
        }}
        .progress {{
            height: 20px;
            background-color: #4CAF50;
            border-radius: 4px;
            text-align: center;
            color: white;
            line-height: 20px;
        }}
    </style>
</head>
<body>
    <h1>Task Manager Documentation Coverage Report</h1>
    <h2>Generated on April 11, 2025</h2>

    <h2>Overall Coverage: {:.2f}%</h2>
    <div class="progress-bar">
        <div class="progress" style="width: {:.2f}%">{:.2f}%</div>
    </div>

    <h2>File Coverage Details</h2>
    <table>
        <tr>
            <th>File</th>
            <th>Documented Lines</th>
            <th>Total Lines</th>
            <th>Coverage</th>
        </tr>
'''.format(coverage_percent, coverage_percent, coverage_percent)

    sorted_files = sorted(
        [(file, data) for file, data in coverage_data.items()],
        key=lambda x: (x[1]['covered'] / x[1]['total']) if x[1]['total'] > 0 else 0
    )

    for file, data in sorted_files:
        file_name = os.path.basename(file)
        percent = (data['covered'] / data['total'] * 100) if data['total'] > 0 else 0

        css_class = "low"
        if percent >= 80:
            css_class = "high"
        elif percent >= 50:
            css_class = "medium"

        html += '''        <tr class="{}">
            <td>{}</td>
            <td>{}</td>
            <td>{}</td>
            <td>{:.2f}%</td>
        </tr>
'''.format(css_class, file_name, data['covered'], data['total'], percent)

    html += '''    </table>
</body>
</html>'''

    with open(os.path.join(output_dir, "index.html"), 'w') as f:
        f.write(html)

    with open(os.path.join(output_dir, "coverxygen.json"), "w") as json_file:
        json.dump([
            {
                "file": file,
                "covered": data["covered"],
                "total": data["total"],
                "coverage": round((data["covered"] / data["total"]) * 100, 2) if data["total"] > 0 else 0
            }
            for file, data in coverage_data.items()
        ], json_file, indent=4)

    print(f"Report written to: {output_dir}")

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: python coverage_report_lcov.py [lcov_file] [primary_output_dir]")
        sys.exit(1)

    with open(sys.argv[1], 'r') as f:
        lcov_data = f.read()

    coverage_data = parse_lcov_data(lcov_data)
    primary_output = sys.argv[2]
    secondary_output = "coverxygen"

    create_html_report(coverage_data, primary_output)
    create_html_report(coverage_data, secondary_output)