import json
import os
import sys

def create_html_report(json_file, output_dir):
    with open(json_file, 'r') as f:
        data = json.load(f)
    
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
    
    html = """
    <!DOCTYPE html>
    <html>
    <head>
        <title>Doxygen Coverage Report</title>
        <style>
            body { font-family: Arial, sans-serif; margin: 20px; }
            table { border-collapse: collapse; width: 100%; }
            th, td { text-align: left; padding: 8px; border: 1px solid #ddd; }
            tr:nth-child(even) { background-color: #f2f2f2; }
            th { background-color: #4CAF50; color: white; }
            .low { background-color: #ffcccc; }
            .medium { background-color: #ffffcc; }
            .high { background-color: #ccffcc; }
            h1, h2 { color: #333; }
        </style>
    </head>
    <body>
        <h1>Doxygen Documentation Coverage Report</h1>
        <h2>Summary</h2>
        <table>
            <tr>
                <th>Symbol</th>
                <th>Type</th>
                <th>Line</th>
                <th>Documented</th>
                <th>Coverage</th>
            </tr>
    """
    
    for item in data:
        color_class = "high" if item["documented"] else "low"
        html += f"""
        <tr class="{color_class}">
            <td>{item["symbol"]}</td>
            <td>{item["kind"]}</td>
            <td>{item["line"]}</td>
            <td>{"Yes" if item["documented"] else "No"}</td>
            <td>{100 if item["documented"] else 0}%</td>
        </tr>
        """
    
    html += """
        </table>
    </body>
    </html>
    """
    
    with open(os.path.join(output_dir, "index.html"), 'w') as f:
        f.write(html)
    
    print(f"HTML report created at {os.path.join(output_dir, 'index.html')}")

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: python coverage_report.py [json_file] [output_dir]")
        sys.exit(1)
    
    create_html_report(sys.argv[1], sys.argv[2])